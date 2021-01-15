# Verbs-of-movement
Final project for ALAO/Java courses

The idea of the project and detailed description of the scripts can be found in Rapport ALAO.pdf file. 

1) If you want to test Java files, please, make sure that you have basic CoreNLP package installed. 
Tutorial can be found here (https://fahadusman.com/getting-started-with-stanford-corenlp/)
Russian version works uses some of the classes of english version.

2) You also need to download standford-russian-corenlp-models.jar from this site (https://stanfordnlp.github.io/CoreNLP/model-zoo.html)
After that, please add this file to lib folder of the project. File pom.xml has already all dependencies added, so you don't need to change it.

3) To test request_ruscorpora.py called from Java file, please make sure you changed the path to yours in the third exercise in two places.

    
		String filepath = "/Users/Alena/Desktop/ALAO/Sentences1";
    
 Here you need to indicate correct path to python also.
    
	  process = rt.exec(String.format("/usr/local/bin/python3 /Users/Alena/Desktop/ALAO/request_ruscorpora.py -v %s -n %s", translation1.getsamplelemma(), filepath));
    
 4) If you see LOG errors, you don't need to pay attention to them and wait for the execution. 
 CoreNLP package works slowly, but LOG errors are not important. 
