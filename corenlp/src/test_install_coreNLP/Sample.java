package test_install_coreNLP;

import java.io.*;
import java.util.*;

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


public class Sample {

	//OBJECTS//

   public Annotation sample;
   public Annotation document;
   public StanfordCoreNLP pipeline;
   public String samplelemma = "";
   public String lemma = "";
   public String samplepos = "";
   public String pos = "";
   public HashMap <String,String> samplemorpho = new HashMap<String, String>();
   public HashMap <String,String> morpho = new HashMap<String, String>();
   public String sampletense = "";
   public String tense = "";
   public String sampleaspect = "";
   public String aspect = "";

	//CONSTRUCTOR//

	public Sample () {

	Properties props = new Properties();

	props.setProperty("annotators", "tokenize, ssplit, pos, custom.lemma, custom.morpho");
	props.setProperty("tokenize.language","English");
	props.setProperty("pos.model","edu/stanford/nlp/models/pos-tagger/russian-ud-pos.tagger");
	props.setProperty("customAnnotatorClass.custom.lemma", "edu.stanford.nlp.international.russian.process.RussianLemmatizationAnnotator");
	props.setProperty("customAnnotatorClass.custom.morpho", "edu.stanford.nlp.international.russian.process.RussianMorphoAnnotator");
	props.setProperty("tokenize.verbose","false");


	this.pipeline = new StanfordCoreNLP(props);
	Annotator russianMorphoAnnotator;
	russianMorphoAnnotator = new RussianMorphoAnnotator();
	this.pipeline.addAnnotator(russianMorphoAnnotator);


	}

	//METHODS//

	//****************************return annotated text***************************//
	public String annotatetranslation(String translation) {
		this.sample = new Annotation(translation);
		this.pipeline.annotate(this.sample);
		return translation;
	  }

	public Annotation annotatetext(String text) {
		this.document = new Annotation(text);
		this.pipeline.annotate(this.document);
		return this.document;
	  }


	//**********************************return lemmas***************************//
	public String getsamplelemma() {
	List<CoreMap> sentences = sample.get(CoreAnnotations.SentencesAnnotation.class);
	for (CoreMap sentence : sentences) {
	Tree parseTree = sentence.get(TreeAnnotation.class);

	 for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
	 samplepos = token.get(PartOfSpeechAnnotation.class);
	 if (samplepos.equals("VERB")) {
	 samplelemma = token.lemma();
	 }
    }
   }
	return samplelemma;
  }


	public String getlemma() {
		List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
		for (CoreMap sentence : sentences) {
		Tree parseTree = sentence.get(TreeAnnotation.class);

		 for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
		 pos = token.get(PartOfSpeechAnnotation.class);
		 if (pos.equals("VERB")) {
		 lemma = token.lemma();
		 }
	    }
	   }
	  return lemma;
	}

	//**********************************return tenses***************************//

	public String getsampletense() {
		List<CoreMap> sentences = sample.get(CoreAnnotations.SentencesAnnotation.class);
		for (CoreMap sentence : sentences) {
		Tree parseTree = sentence.get(TreeAnnotation.class);

		 for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
		 samplepos = token.get(PartOfSpeechAnnotation.class);
		 if (samplepos.equals("VERB")) {
		 samplemorpho = token.get(CoreAnnotations.CoNLLUFeats.class);
		 sampletense = samplemorpho.get("Tense");
		 }
	    }
	   }
	return sampletense;

 }
	public String gettense() {
		List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
		for (CoreMap sentence : sentences) {
		Tree parseTree = sentence.get(TreeAnnotation.class);

		 for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
		 pos = token.get(PartOfSpeechAnnotation.class);
		 if (pos.equals("VERB")) {
		 morpho = token.get(CoreAnnotations.CoNLLUFeats.class);
		 tense = morpho.get("Tense");

		 }
	    }
	   }
	return tense;

 }
	//**********************************return aspects***************************//
	public String getsampleaspect() {
		List<CoreMap> sentences = sample.get(CoreAnnotations.SentencesAnnotation.class);
		for (CoreMap sentence : sentences) {
		Tree parseTree = sentence.get(TreeAnnotation.class);

		 for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
		 samplepos = token.get(PartOfSpeechAnnotation.class);
		 if (samplepos.equals("VERB")) {
		 samplemorpho = token.get(CoreAnnotations.CoNLLUFeats.class);
		 sampleaspect = samplemorpho.get("Aspect");

		 }
	    }
	   }
		return sampleaspect;
    }

	public String getaspect() {
		List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
		for (CoreMap sentence : sentences) {
		Tree parseTree = sentence.get(TreeAnnotation.class);

		 for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
		 pos = token.get(PartOfSpeechAnnotation.class);
		 if (pos.equals("VERB")) {
		 morpho = token.get(CoreAnnotations.CoNLLUFeats.class);
		 aspect = morpho.get("Aspect");

		 }
	    }
	   }
		return aspect;
    }


}