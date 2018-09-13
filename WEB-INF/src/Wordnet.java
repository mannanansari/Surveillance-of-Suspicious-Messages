import java.io.*;
import java.util.*;
import java.lang.String;
import java.lang.*;
import edu.smu.tspell.wordnet.*;
public class Wordnet
{
public static String[] word(String wd)
{
String[] st = {wd};
String delima = "[\\s]+";
String[] strr = wd.split(delima);
for(String wordForm : strr)
{
ArrayList <String> al = new ArrayList <String>();
HashSet hs = new HashSet();
File f = new File ("C:/Program Files/WordNet/2.1/dict");
System.setProperty("wordnet.database.dir", f.toString());
WordNetDatabase database = WordNetDatabase.getFileInstance();
Synset[] synsets = database.getSynsets(wordForm);
if (synsets.length > 0)
{
for (int i = 0; i < synsets.length; i++)
{
String[] wordForms = synsets[i].getWordForms();
for (int j = 0; j < wordForms.length; j++)
al.add(wordForms[j]);
hs.addAll(al);
al.clear();
al.addAll(hs);
}
String[] str = al.toArray(new String[al.size()]);
return str;
}
}
return st;
}
}