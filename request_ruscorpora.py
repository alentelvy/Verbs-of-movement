import requests
import argparse
import os

#function to find cooccurrences in russian corpus 
def do_request(verb): 
    link = 'https://processing.ruscorpora.ru/search.xml?env=alpha&api=1.0&mycorp=&mysent=&mysize=&mysentsize=&dpp=&spp=&spd=&mydocsize=&mode=main&lang=ru&sort=i_grtagging&nodia=1&text=lexgramm&parent1=0&level1=0&lex1='
    link += verb
    link += '&gramm1=praes&sem1=&flags1=&sem-mod1=sem&sem-mod1=sem2&parent2=0&level2=0&min2=1&max2=1&lex2=&gramm2=&sem2=&flags2=&sem-mod2=sem&sem-mod2=sem2'
    page = requests.get(link)
    contents = page.text
    t = 0
    Statements = []
    Statement = ''
    add = False
    for i in range(5000):                    #take first 1000 words of the corpus on the page 
        t1 = contents[t:].find('b-wrd-expl') #find tags with words 
        t2 = contents[t+t1:].find('>')
        t3 = contents[t+t1:].find('</span>')
        t4 = contents[t+t1:].find('<span')
        word = contents[t+t1+t2+1:t+t1+t3]
        
        # print(contents[t+t1+t3:t+t1+t4])
        if t4 - t3 < 14 and t4 - t3 > 7:
            word += contents[t+t1+t3+7:t+t1+t4] #punctuation marks and spaces
        # words.append(word)
        if len(word) > 0 and word[0].isupper() and len(Statement) > 0 and (Statement[-1] != ' ' or Statement[-2:] == '  '): #if the word starts with capital letter, we add current sentence to the list and start treating a new one
            if add and Statement not in Statements and len([s for s in Statement.split(" ") if len(s) > 1 ]) > 2 :
                Statements.append(Statement)
            Statement = ''
            add = False

        if contents[t+t1:t+t1+20].find('g-em') > -1: #if the word is highlited(has tag g-em) -> it's the needed one, we add a sentence with it
            add = True
            
        Statement += word #add the current word in the current sentence
        if ((Statement.find('. ') > -1 and Statement.find(' т.') == -1 and Statement.find(' им.') == -1) or Statement.find('! ') > -1 or Statement.find('? ') > -1 )  and len(Statement) > 0: #check if the sentence finishes with the last word and if so we add current sentence to the list and start treating a new one
            if add and Statement not in Statements and len([s for s in Statement.split(" ") if len(s) > 1 ]) > 2:
                Statements.append(Statement)
            Statement = ''
            add = False
            
        t += t1+t3
        if len(Statements) > 0 and  len(Statements[-1]) < 4:  #if a small piece of text (not a real sentence) was added -> delete it 
            Statements = Statements[:-1]
            break
            
    return Statements


def main():
    if_new_file = True
    sentences = do_request(FLAGS.verb)
    m = 'w'
    if os.path.exists(FLAGS.name) and not if_new_file:
        m = 'a'
    with open(FLAGS.name, m) as f:
        for s in sentences:
            print(s, len(s.split(" ")))
            f.write(s + '\n')
    

 

if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument(
        '-v',
        '--verb',
        type=str,
        default="приходить",
        help='Глагол'
    )

    parser.add_argument(
        '-n',
        '--name',
        type=str,
        default="Sentences",
        help='Filename'
    )


    FLAGS = parser.parse_args()
    main()