import org.apache.commons.lang3.time.StopWatch;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
//import java.util.Vector;

public class SemanticMain {
    public List<String> listVocabulary = new ArrayList<>();  //List that contains all the vocabularies loaded from the csv file.
    public List<double[]> listVectors = new ArrayList<>(); //Associated vectors from the csv file.
    public List<Glove> listGlove = new ArrayList<>();
    public final List<String> STOPWORDS;

    public SemanticMain() throws IOException {
        STOPWORDS = Toolkit.loadStopWords();
        Toolkit.loadGLOVE();
    }


    public static void main(String[] args) throws IOException {
        StopWatch mySW = new StopWatch();
        mySW.start();
        SemanticMain mySM = new SemanticMain();
        mySM.listVocabulary = Toolkit.getListVocabulary();
        mySM.listVectors = Toolkit.getlistVectors();
        mySM.listGlove = mySM.CreateGloveList();

        List<CosSimilarityPair> listWN = mySM.WordsNearest("computer");
        Toolkit.PrintSemantic(listWN, 5);

        listWN = mySM.WordsNearest("phd");
        Toolkit.PrintSemantic(listWN, 5);

        List<CosSimilarityPair> listLA = mySM.LogicalAnalogies("china", "uk", "london", 5);
        Toolkit.PrintSemantic("china", "uk", "london", listLA);

        listLA = mySM.LogicalAnalogies("woman", "man", "king", 5);
        Toolkit.PrintSemantic("woman", "man", "king", listLA);

        listLA = mySM.LogicalAnalogies("banana", "apple", "red", 3);
        Toolkit.PrintSemantic("banana", "apple", "red", listLA);
        mySW.stop();
        if (mySW.getTime() > 2000)
            System.out.println("It takes too long to execute your code!\nIt should take less than 2 second to run.");
        else
            System.out.println("Well done!\nElapsed time in milliseconds: " + mySW.getTime());
    }

    public List<Glove> CreateGloveList() {
        List<Glove> listResult = new ArrayList<>();

        for(int i=0;i<Toolkit.getListVocabulary().size();i++) {
            boolean checker = true;
            for(int j = 0; j < STOPWORDS.size() && checker; j++){
                if(Objects.equals(Toolkit.getListVocabulary().get(i), STOPWORDS.get(j))) {
                    checker = false;
                }
            }
            if(checker) {
                Vector tempVector = new Vector(Toolkit.getlistVectors().get(i));
                Glove tempGlove = new Glove(Toolkit.getListVocabulary().get(i),tempVector);
                listResult.add(tempGlove);
            }


        }

        return listResult;
    }

    public List<CosSimilarityPair> WordsNearest(String _word) {
        List<CosSimilarityPair> listCosineSimilarity = new ArrayList<>();

        List<Glove> gloveList = CreateGloveList();
        Vector tempVector = null;
        boolean errorChecker = true;
        for(int i=0; i<gloveList.size();i++) {
            if (Objects.equals(_word, gloveList.get(i).getVocabulary())) {
                tempVector = gloveList.get(i).getVector();
                errorChecker = false;
            }
        }
        for(int i=0; errorChecker && i<gloveList.size();i++) {
            if(Objects.equals("error", gloveList.get(i).getVocabulary())) {
                tempVector=gloveList.get(i).getVector();
            }
        }
        if(!errorChecker) {
            for(int i=0;i<gloveList.size();i++) {
                if(!Objects.equals(tempVector, gloveList.get(i).getVector())) {
                    double tempCS = tempVector.cosineSimilarity(gloveList.get(i).getVector());
                    CosSimilarityPair tempPair = new CosSimilarityPair(_word, gloveList.get(i).getVocabulary(), tempCS);
                    listCosineSimilarity.add(tempPair);
                }
            }
        }
        else {
            for(int i=0;i<gloveList.size();i++) {
                if(!Objects.equals(tempVector, gloveList.get(i).getVector())) {
                    double tempCS = tempVector.cosineSimilarity(gloveList.get(i).getVector());
                    CosSimilarityPair tempPair = new CosSimilarityPair("error", gloveList.get(i).getVocabulary(), tempCS);
                    listCosineSimilarity.add(tempPair);
                }
            }
        }
        HeapSort.doHeapSort(listCosineSimilarity);
        return listCosineSimilarity;
    }

    public List<CosSimilarityPair> WordsNearest(Vector _vector) {
        List<CosSimilarityPair> listCosineSimilarity = new ArrayList<>();

        List<Glove> gloveList = CreateGloveList();
        double greatestCS = 0.0;
        Vector tempVector = null;
        String tempWord = null;
        boolean errorChecker = true;
        for(int i=0; i<gloveList.size();i++) {
            if (Objects.equals(_vector, gloveList.get(i).getVector())) {
                tempVector = gloveList.get(i).getVector();
                tempWord = gloveList.get(i).getVocabulary();
                errorChecker = false;
            }
        }
        for(int i=0; errorChecker && i<gloveList.size();i++) {
            double tempCS = _vector.cosineSimilarity(gloveList.get(i).getVector());
            if (tempCS>greatestCS) {
                greatestCS = tempCS;
                tempWord = gloveList.get(i).getVocabulary();
            }
        }
        if(!errorChecker){
            for(int i=0;i<gloveList.size();i++) {
                if(!Objects.equals(tempVector, gloveList.get(i).getVector())) {
                    double tempCS = tempVector.cosineSimilarity(gloveList.get(i).getVector());
                    CosSimilarityPair tempPair = new CosSimilarityPair(tempWord, gloveList.get(i).getVocabulary(), tempCS);
                    listCosineSimilarity.add(tempPair);
                }
            }
        }
        else {
            for(int i=0;i<gloveList.size();i++) {
                double tempCS = _vector.cosineSimilarity(gloveList.get(i).getVector());
                CosSimilarityPair tempPair = new CosSimilarityPair(tempWord, gloveList.get(i).getVocabulary(), tempCS);
                listCosineSimilarity.add(tempPair);
            }
        }

        HeapSort.doHeapSort(listCosineSimilarity);
        return listCosineSimilarity;
    }

    /**
     * Method to calculate the logical analogies by using references.
     * <p>
     * Example: uk is to london as china is to XXXX.
     *       _firISRef  _firTORef _secISRef
     * In the above example, "uk" is the first IS reference; "london" is the first TO reference
     * and "china" is the second IS reference. Moreover, "XXXX" is the vocabulary(ies) we'd like
     * to get from this method.
     * <p>
     * If _top <= 0, then returns an empty listResult.
     * If the vocabulary list does not include _secISRef or _firISRef or _firTORef, then returns an empty listResult.
     *
     * @param _secISRef The second IS reference
     * @param _firISRef The first IS reference
     * @param _firTORef The first TO reference
     * @param _top      How many vocabularies to include.
     */
    public List<CosSimilarityPair> LogicalAnalogies(String _secISRef, String _firISRef, String _firTORef, int _top) {
        List<CosSimilarityPair> listResult = new ArrayList<>();
        List<Glove> gloveList = CreateGloveList();
        boolean errorChecker = true;
        boolean errorChecker2 = true;
        boolean errorChecker3 = true;
        Vector firISVector = null;
        Vector firTOVector = null;
        Vector secISVector = null;
        for(int i=0; i<gloveList.size();i++) {
            if (Objects.equals(_firISRef, gloveList.get(i).getVocabulary())) {
                firISVector = gloveList.get(i).getVector();
                errorChecker = false;
            } else if (Objects.equals(_firTORef, gloveList.get(i).getVocabulary())) {
                firTOVector = gloveList.get(i).getVector();
                errorChecker2 = false;
            } else if (Objects.equals(_secISRef, gloveList.get(i).getVocabulary())) {
                secISVector = gloveList.get(i).getVector();
                errorChecker3 = false;
            }
        }
        if (!errorChecker && !errorChecker2 && !errorChecker3) {
            Vector firRelation = firISVector.subtraction(firTOVector);
            Vector secTOVector = secISVector.subtraction(firRelation);
            List<CosSimilarityPair> secTOList = WordsNearest(secTOVector);
            for (int i = 0; i < _top; i++) {
                if ((!Objects.equals(secTOList.get(i).getWord2(), _firISRef)) && (!Objects.equals(secTOList.get(i).getWord2(), _firTORef)) && (!Objects.equals(secTOList.get(i).getWord2(), _secISRef))) {
                    listResult.add(secTOList.get(i));
                } else {
                    _top++;
                }
            }
        }
        return listResult;
    }
}