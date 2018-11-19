import java.io.*;
import java.util.*;

class FileStats
{
	private Scanner input;
	private ArrayList <String> wordList=new ArrayList<String>();
	private HashSet <String> wordSet=new HashSet<String>();
	private ArrayList <Entry<String>> entryList=new ArrayList<Entry<String>>();
	private Map <String, Character> dictionary=new TreeMap<String, Character>();

	private class Entry <T> implements Comparable<Entry<T>>
    {
		public T word;
		public int frequency;
		public Entry(T word, int f)
        {
			this.word=word;
			frequency=f;
		}
		public int compareTo(Entry<T> e)
		{
			return Integer.compare(e.frequency, this.frequency);
		}
	}

	public FileStats(String path)
    {
		
		String line = null;
		try
		{
			input = new Scanner(new File(path));
		}
		catch(FileNotFoundException e)
		{
			System.err.println("Error opening file..");
			System.exit(1);
		}
		
		//Code using StringTokenizer
		StringTokenizer st;
		String token;
		try
		{
			while((line = input.nextLine()) != null)
			{
				st = new StringTokenizer(line);
				while(st.hasMoreTokens())
				{
					token = st.nextToken();
					token = token.replace(",", "");
					token = token.replace(".", "");
					token = token.replace("!", "");
					token = token.replace("\'", "");
					token = token.replace("/", "");
					wordList.add(token.toLowerCase());
					wordSet.add(token.toLowerCase());
				}
			}
		}
		catch(NoSuchElementException e)
		{
		
		}
		
		//Code using .split(). Automatically removes all non-alphabetical characters.
        /*
        String[] test;
        try
        {
            while((line = input.nextLine()) != null)
            {
                test = line.split("\\W+");
                for(String word : test)
                {
                    wordList.add(word.toLowerCase());
                    wordSet.add(word.toLowerCase());
                }
            }
        }
        catch(NoSuchElementException e)
        {
        
        }
        */
		
		count(4);
	}

	/*
	 * This method is supposed to
	 * 1. find the frequency of each word in the file.
	 * 2. display the four most frequent words and their frequencies.
	 * 3. construct the dictionary that four key-value pairs. The keys
	 *    are the most frequent words and the values are the characters,
	 *    used to represent the words.
	 */
	private void count(int num)
    {
        ArrayList<Character> charList = new ArrayList<>(Arrays.asList('%', '$', '#', '*'));
		for(String word:wordSet)
		{
			entryList.add(new Entry<>(word, Collections.frequency(wordList, word)));
		}
		
		Collections.sort(entryList);
    
        for(int i = 0; i < num && i < entryList.size(); i++)
        {
            System.out.println(entryList.get(i).word + " appears " +
                    entryList.get(i).frequency + " time(s).");
            dictionary.put(entryList.get(i).word, charList.get(i));
        }
	}
	
	public void printDictionary()
    {
        System.out.println("Key____________Value");
        for(String key : dictionary.keySet())
        {
            System.out.println(key + "------------" + dictionary.get(key));
        }
    }

	public Map<String, Character> getDictionary()
    {
		return dictionary;
	}
}

class FileCompressor
{
	public static void compress(String src, String dest,
			Map<String, Character> dictionary)
    {
        Scanner in = null;
        BufferedWriter out = null;
        try
        {
            in = new Scanner(new File(src));
            out = new BufferedWriter(new FileWriter(new File(dest)));
        }
        catch(IOException e)
        {
            System.err.println("Error opening input file...");
            System.exit(1);
        }
        
        String line;
        try
        {
            while((line = in.nextLine()) != null)
            {
                for(Map.Entry<String, Character> pair :dictionary.entrySet())
                {
                    line = line.replace(pair.getKey(), "" + pair.getValue());
                    line = line.replace(pair.getKey().substring(0, 1).toUpperCase() +
                            pair.getKey().substring(1), "" + pair.getValue());
                }
                out.write(line);
                out.newLine();
            }
        }
        catch(NoSuchElementException e)
        {
        
        }
        catch(IOException e)
        {
            System.err.println("Error writing to output file...");
            System.exit(1);
        }
    
        try
        {
            out.close();
        }
        catch(IOException e)
        {
            System.err.println("Error writing to output file...");
            System.exit(1);
        }
	}

	public static void decompress(String src, String dest,
			Map<Character, String> dictionary)
    {
    
        Scanner in = null;
        BufferedWriter out = null;
        try
        {
            in = new Scanner(new File(src));
            out = new BufferedWriter(new FileWriter(new File(dest)));
        }
        catch(IOException e)
        {
            System.err.println("Error opening output file...");
            System.exit(1);
        }
    
        String line;
        try
        {
            while((line = in.nextLine()) != null)
            {
                for(Map.Entry<Character, String> pair :dictionary.entrySet())
                {
                    line = line.replace(pair.getKey() + "", pair.getValue());
                }
                out.write(line);
                out.newLine();
            }
        }
        catch(NoSuchElementException e)
        {
        
        }
        catch(IOException e)
        {
            System.err.println("Error writing to output file...");
            System.exit(1);
        }
        
        
        try
        {
            out.close();
        }
        catch(IOException e)
        {
            System.err.println("Error writing to output file...");
            System.exit(1);
        }
	}
}

public class FileIO {
	public static void main(String args[]) throws IOException{
		FileStats fs = new FileStats("basketball.txt");
		fs.printDictionary();

		Map <String, Character> m1 = fs.getDictionary();
		FileCompressor.compress("basketball.txt", "compressed.txt", m1);
  
		//Could not get it to work with Map.
        HashMap <Character, String> m2 = new HashMap<>();
        try
        {
            for(String key : m1.keySet())
            {
                m2.put(m1.get(key), key);
            }
        }
        catch(UnsupportedOperationException e)
        {
            System.out.println("Oops");
        }
        FileCompressor.decompress("compressed.txt", "decompressed.txt",m2);
	}
}
