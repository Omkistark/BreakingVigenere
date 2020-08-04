import java.util.*;
import edu.duke.*;
import java.io.*;
public class VigenereBreaker
{
    public String sliceString(String message, int whichSlice, int totalSlices)
    {
        //REPLACE WITH YOUR CODE
        StringBuilder slicedStr=new StringBuilder();
        for(int i=whichSlice;i<message.length();i=i+totalSlices)
        {
            slicedStr.insert(0,message.charAt(i));
        }
        slicedStr.reverse();
        return slicedStr.toString();
    }
    public HashSet<String> readDictionary(FileResource fr)
    {
        HashSet<String> hs=new HashSet<String>();
        for(String str : fr.lines())
        {
            str=str.toLowerCase();
            hs.add(str);
        }
        return hs;
    }
    public int countWords(String message,HashSet<String> dictionary)
    {
        int count=0;
        for(String word:message.split("\\W"))
        {
            word=word.toLowerCase();
            //System.out.println(word);
            if (dictionary.contains(word))
                count++;
        }
        return count;
    }
    public String breakForLanguage(String encrypted,HashSet<String> dictionary)
    {
        String decrypted=null;
        int[] key=null;
        int max=0;
        for(int i=1;i<=100;i++)
        {
            key=tryKeyLength(encrypted,i,mostCommonCharIn(dictionary));
            //System.out.print(key.length);
            VigenereCipher vc=new VigenereCipher(key);
            int count=countWords(vc.decrypt(encrypted),dictionary);
            if(count>max)
            {
                max=count;
                decrypted=vc.decrypt(encrypted);
                System.out.print("Key Size = "+key.length+"\nKey Used: [");
                for(int k=0;k<key.length;k++)
                    System.out.print(key[k]+" ");
                System.out.println("]");
            }
        }
        System.out.println("Valid Words: "+max);
        return decrypted;
    }
    public int[] tryKeyLength(String encrypted, int klength, char mostCommon)
    {
        int[] key = new int[klength];
        CaesarCracker cc=new CaesarCracker(mostCommon);
        for(int i=0;i<klength;i++)
        {
            key[i]=cc.getKey(sliceString(encrypted,i,klength));
        }
        //for(int i=0;i<klength;i++)
        //    System.out.print(key[i]+" ");
        return key;
    }

    public void breakVigenere ()
    {
        //FileResource dictionaryFile=new FileResource("F:/Programs/JAVA/VigenereProgram/dictionaries/English");
        HashMap<String,HashSet<String>> languages=new HashMap<String,HashSet<String>>();
        String[] dictionaries={"Danish","Dutch","English","French","German","Italian","Portuguese","Spanish"};
        //DirectoryResource dr=new DirectoryResource();
        for(String str:dictionaries)
        {
            FileResource dictionaryFile=new FileResource("F:/Programs/JAVA/VigenereProgram/dictionaries/"+str);
            System.out.println("Reading "+str+" Dictionary");
            HashSet<String> dictionary=readDictionary(dictionaryFile);
            languages.put(str,dictionary);
        }
        FileResource fr=new FileResource();
        String message=fr.asString();
        breakForAllLangs(message,languages);
        //System.out.println(countWords(message,dictionary));
        //System.out.println(dictionary.size());
        //int[] key=tryKeyLength(message,4,'e');
        //VigenereCipher vc=new VigenereCipher(key);
        //System.out.println(breakForLanguage(message,dictionary));
    }
    public char mostCommonCharIn(HashSet<String> dictionary)
    {
        char common='\0';
        int max=0;
        HashMap<Character,Integer> hm=new HashMap<Character,Integer>();
        for(String str:dictionary)
        {
            for(int i=0;i<str.length();i++)
            {
                char curr=str.charAt(i);
                if(hm.containsKey(curr))
                    hm.put(curr,hm.get(curr)+1);
                else
                    hm.put(curr,1);
            }
        }
        for(char c:hm.keySet())
        {
            int curr=hm.get(c);
            if(curr>max)
            {
                max=curr;
                common=c;
            }
        }
        return common;
    }
    public void quiz()
    {
        //FileResource fr=new FileResource();
        //String message=fr.asString();
        FileResource dictionaryFile=new FileResource("F:/Programs/JAVA/VigenereProgram/dictionaries/Spanish");
        HashSet<String> dictionary=readDictionary(dictionaryFile);
        //int[] key=tryKeyLength(message,38,'e');
        //System.out.print(key.length);
        //VigenereCipher vc=new VigenereCipher(key);
        // int count=countWords(vc.decrypt(message),dictionary);
        System.out.println("Most Common Word:  "+mostCommonCharIn(dictionary));
    }
    public void breakForAllLangs(String encrypted,HashMap<String,HashSet<String>> languages)
    {
        int max=0;
        String correctLanguage="";
        for(String selectedLanguage: languages.keySet())
        {
            int count=countWords(breakForLanguage(encrypted,languages.get(selectedLanguage)),languages.get(selectedLanguage));
            if(count>max)
            {
                max=count;
                correctLanguage=selectedLanguage;
            }
        }
        System.out.println("Correct Language is: "+correctLanguage);
        System.out.println(breakForLanguage(encrypted,languages.get(correctLanguage)));
    }
}
