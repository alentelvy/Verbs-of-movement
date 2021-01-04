import requests
import argparse
from googletrans import Translator



#function to check if the verb has attirbute of movement in wiktionary
def check_if_verb_of_movement(verb): 
    movement_meaning = ['перемещать', 'перемещаться', 'двигаться', 'двигать'] #attributes of movement
    link  = 'https://ru.wiktionary.org/wiki/'
    link += verb
    page = requests.get(link)
    text_page = page.text
    verb_of_mov = False
    for m in movement_meaning:     #if any attribute of movement was found
        if text_page.find(m) > -1: #if the method finds any index (bigger than -1)
            verb_of_mov = True     #return true and finish
            break
    return verb_of_mov


#function to find all the forms for verbs of movement on the site https://wordroot.ru/
def find_words_of_the_same_root(init_word):
    link  = 'https://wordroot.ru/'
    link += init_word
    page = requests.get(link)
    text_page = page.text
    stop = False
    t = 0
    words = []
    endings = ['еть','оть','ить','ыть','ать','уть','ять'] #find only verbs (using list of endings for verbs) among all the forms
    endings += [e + 'ся' for e in endings]                #find reflective verbs also 
    endings += ['ти']                                     #find verbs with 'ти' ending
    while not stop:
        if text_page[t:].find('омонимичными') == -1 and text_page.find('омонимичными') > -1 : #skip homonymous roots
            stop = True
            break
        t1 = text_page[t:].find('<li><span>')             #find the beginning of  html tag <li><span>
        
        if t1 == -1:                                      #if there are no words to check, finish
            stop = True
            break
        t2 = text_page[t+t1:].find('</span>')             #find the end of html tag </span>
        word = text_page[t+t1+len('<li><span>'):t+t1+t2]  #read everything in between two tags
        if len(word) > 0:                                 #if the tag is not empty
            if_verb = False                               #set bool to False as we suppose it's not a verb
            for e in endings:                             #check all the endings
                if len(word) > len(e) and word[-len(e):] == e: #check that the length of verb is longer that the length of ending, check that the ending of this word is equal to ending 
                    if_verb = True                         

            if if_verb and check_if_verb_of_movement(word): #if it's a verb and it's a verb of movement, add it to words 
                words.append(word)
        t += t1+t2                                          #pass over the html tag 
    return words  

#delete quotes from the corpus        
def delete_quotes(str1): 
    for char in "', ":
        str1 = str1.replace(char, "")
    return str1

#get forms of verbs mentioned in the list of infinitives 
def get_verbs_of_movement(filename, verbs_filename = ""):
    translator = Translator()                              #initializing Translator() class to use it later with google translate package
    if verbs_filename == "":                               #if there is no file with verbs, take the list of verbs below and create it 
        main_verbs  = []
        main_verbs += ['бежать', 'бегать']
        main_verbs += ['ехать', 'ездить']
        main_verbs += ['идти', 'ходить']
        main_verbs += ['лететь', 'летать']
        main_verbs += ['плыть', 'плавать']
        main_verbs += ['тащить', 'таскать']
        main_verbs += ['катить', 'катать']
        main_verbs += ['катиться', 'кататься']
        main_verbs += ['нести', 'носить']
        main_verbs += ['нестись', 'носиться']
        main_verbs += ['вести', 'водить']
        main_verbs += ['везти', 'возить']
        main_verbs += ['ползти', 'ползать']
        main_verbs += ['лезть', 'лазить']
        main_verbs += ['лазать']
        main_verbs += ['брести', 'бродить']
        main_verbs += ['гнать', 'гонять']
        main_verbs += ['гнаться', 'гоняться']
        main_verbs += ['двигать', 'двигаться']
        main_verbs += ['перемещать', 'перемещаться']
        all_verbs = set()                           #store only unique elements 
        for mv in main_verbs:
            words = find_words_of_the_same_root(mv) #find forms of this verb 
            all_verbs = all_verbs.union(set(words)) #add to all_verbs new forms 
            all_verbs = all_verbs.union(set([mv]))  #add the verb itself
        all_verbs = list(all_verbs)                 #turn set into list
    else:
        with open(verbs_filename, 'r') as f:        #if the file mentioned 
            all_verbs = f.read().split('\n')        #read file and split into lines
        all_verbs = [delete_quotes(a) for a in all_verbs if len(a) > 2] #delete all the caracters except from words, delete empty lines 
        
    
    #put the verbs in alphabet order 
    all_verbs = sorted(all_verbs)

    #get the transaltion from google translate (from googletrans import Translator)
    from_language = 'ru'
    to_language = 'fr'
    translations = {}
    for v in all_verbs:
        translation = translator.translate(v, src=from_language, dest=to_language).text #call class translate and method translator 
        translations[translation] = v 
    # print(all_verbs)
    with open(filename, 'w') as f:                  #open file to write down the verbs and translation
        for translation, verb in translations.items(): 
            f.write( verb + ' '*(30 - len(verb)) + translation + '\n') #format text with 30 spaces - len(verb) between verbs and translation
    return translations.values(), translations.keys()                  #return two lists



def main():
    all_verbs, translations = get_verbs_of_movement(FLAGS.name, verbs_filename = FLAGS.verbs)

if __name__ == '__main__':
    
    parser = argparse.ArgumentParser()
    parser.add_argument(
        '-n',
        '--name',
        type=str,
        default="translations",
        help='Final version'
    )
    parser.add_argument(
        '-v',
        '--verbs',
        type=str,
        default="",
        help='Verbs filename'
    )


    FLAGS = parser.parse_args()
    
    main()