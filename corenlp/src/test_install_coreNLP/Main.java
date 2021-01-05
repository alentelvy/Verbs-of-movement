package test_install_coreNLP;

import java.io.*;
import java.io.File;
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; 
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.Executors;

import edu.stanford.nlp.international.russian.process.RussianMorphoAnnotator;
import edu.stanford.nlp.io.*;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.*;
import edu.stanford.nlp.util.TypesafeMap.Key;



public class Main {

	public static void main(String[] args) throws IOException, InterruptedException {
		
		
		
		//********************************************EX1*********************************************//
		//                    Show french verb and ask for russian translation                       //
		
		String result;
		int cpt = 0;
	    Map<String, String> dico = new HashMap<String, String>();	     
	 
		try {
		      File myObj = new File("verbs_corpus.txt");
		      Scanner myReader = new Scanner(myObj);
		      while (myReader.hasNextLine()) {
		        String line = myReader.nextLine(); 
		        String[] arr = line.split(",");    
		        dico.put(arr[1], arr[0]); 
		        		        	        
		      }
		      //System.out.println(dico);
		      myReader.close();
		    } catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
		
		
		for (Map.Entry<String, String> entry : dico.entrySet()) {
			Scanner input = new Scanner(System.in);
			System.out.print("Give the translation for "+ entry.getKey() +":");
		    String verb = input.next();
			 
			if(verb.equals(entry.getValue())) {
				result = entry.getKey() + " = " + entry.getValue();
				System.out.println("Correct  " + entry.getKey() + " = " + entry.getValue());
				cpt +=1;
				
			} else	{
				
				System.out.println("Error, right answer is "+ entry.getValue());
			}
            
        }
		
		System.out.println("Your score after the first exercise is " + cpt); 
		
		
		//********************************************EX3*********************************************//
		//Show french verb and russian base of verb, check if the prefix corresponds to their meaning// 
		
		String[][] list = { {"entrer", "ходить", "в"}, 
							{"sortir", "ходить", "вы"},
							{"dépasser", "гнать", "обо"},
							{"quitter", "ехать", "у"},
							{"passer", "ехать", "про"},
							{"conduire", "везти", "до"},
							{"se coucher", "ходить", "за"},
							{"divorcer", "вестись", "раз"},
							{"se rassembler", "йтись", "со"},
							{"descendre", "ъехать", "с"},
							
		
		
		
		};
					
		
		for(int i=0; i< list.length; i++) {
            for(int j=0; j< list[i].length; j++) {
            	Scanner input = new Scanner(System.in);
    			System.out.print("Give the prefix for " + list[i][0] + "(" + list[i][1]+ ")");
    		    String prefixe = input.next();
    		    
    		    if(prefixe.equals(list[i][2])) {
    				result = list[i][2]+list[i][1];
    				System.out.println("Correct,  " + list[i][0] + " = " + result);
    				cpt +=1;
    				break;
    				
    		    } else	{
    				result = "Error, right answer is ";
    				System.out.println("Error, right answer is " + list[i][2]+ list[i][1]);
    		}
        }                   
	}         
	
		System.out.println("Your score after the second exercise is " + cpt); 
		
		
		//**************************************EX3*******************************************//
		//Check lemma, tense, aspect in user's answer. Show examples of usage from ruscorpora.//
		
		String[][] tab = {{"Elle est sortie de la piece", "Она вышла из комнаты"}, 
						  {"Il entre la salle", "Он входит в аудиторию"},
						  {"Demain je viendrai à Londres", "Завтра я приеду в Лондон"},
						  {"Ils portent un canapé", "Они несут диван"},
						  {"Il s'est envolé hier", "Он улетел вчера"},
						  {"Quand part le prochain train pour Toulouse ?", "Когда отходит следующий поезд в Тулузу?"},
						  {"Le chat chassait la souris", "Кот гнался за мышью"},
						  {"Le soleil se lève à l'est", "Солнце всходит на востоке"},
						  {"Il a descendu la montagne à skis", "Он съехал с горы на лыжах"},
						  {"Les manifestation se sont rassemblés sur la place", "Демонстранты сошлись на площадь"},
						  {"Nous avons contourné la ville", "Mы объехали город"},
						  {"La voiture a dépassé le cycliste", "Машина обогнала велосипедиста"}};
		
		
		Process process;
		String filepath = "/Users/Alena/Desktop/ALAO/Sentences1";
		for (int i = 0; i < tab.length; i++) {
			
			    //ask for russian translation of french sentence 
			    System.out.println("Give a translation for: "+ tab[i][0]);  
				Scanner input = new Scanner(System.in);                      
				String text = input.nextLine();		

				//take russian verb given by the user and get its parameters
				Sample text1 = new Sample();                                 
				text1.annotatetext(text);							
				text1.getlemma();                                            
				text1.gettense();											 
				text1.getaspect();											 
				
				//take russian verb from manual translation and get its parameters  
				String translation = tab[i][1];                              
				Sample translation1 = new Sample();                          
				translation1.annotatetranslation(translation);
				translation1.getsamplelemma();
				translation1.getsampletense();
				translation1.getsampleaspect();
				
				//compare two verbs
				if (text.equals(translation)) {                               
		 			System.out.println("Correct");
		 			cpt+=1;
		 		}
				
		 		else {
		 			
		 			//compare lemma 
		 			if (!text1.getlemma().equals(translation1.getsamplelemma())) {
		 			
		 				System.out.println("The verb " + text1.getlemma() + " is incorrect");
		 				System.out.println("Right verb is " + translation1.getsamplelemma());
		 				
		 			 }
		 			
		 			//compare tense
		 		     if (!text1.gettense().equals(translation1.getsampletense())) {
			 			
		 				System.out.println("The tense of verb is incorrect. Right tense is " + translation1.getsampletense());	
		 				System.out.println("The tense of your verb is " + text1.gettense());
		 				
		 			}
		 		     
		 			//compare aspect
		 			 if  (!text1.getaspect().equals(translation1.getsampleaspect())) {
			 			
		 				System.out.println("The aspect of verb is incorrect. Right aspect is "+ translation1.getsampleaspect());		 				
		 				System.out.println("The aspect of your verb is " + text1.getaspect());
		 			}
		 			

		 			//show right answer
		 			System.out.println("Right answer is :"+ translation);
		 			
		 			//call for python script to show examples of usage 
		 			Runtime rt = Runtime.getRuntime();
		 			process = rt.exec(String.format("/usr/local/bin/python3 /Users/Alena/Desktop/ALAO/request_ruscorpora.py -v %s -n %s", translation1.getsamplelemma(), filepath));
		 			
		 			Path path = Paths.get(filepath);
		 			process.waitFor();
		 			int exitVal = process.exitValue();
		 			
		 			List<String> lines = Files.readAllLines(path);
		 			    
		 			for (int j = 0; j < Math.min(5,lines.size()); j ++) {
		 			   System.out.println(lines.get(j));
		 			}
		 			
		 			
		 		}

	
		}
		
					System.out.println("Your score after the third exercise is " + cpt);
			
			
}}
