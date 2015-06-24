import nayuki.huffmancoding.*;

import java.io.*;
import java.util.*;
import java.lang.*;

public class Encoder{

  public static void main(String args[]) throws Exception{

      Scanner scan = new Scanner(new File(args[0]));

      ArrayList<Integer> frequencies = getFrequencies(scan);

      int[] test = getArray(frequencies);
      


      /*testing Huffman code found online*/
      FrequencyTable _table = new FrequencyTable(test);
      CodeTree tree = _table.buildCodeTree();
      textGenerator(test);
      System.out.println(tree.toString());





      System.out.println("test frequency table: " + _table.toString());

      //System.out.println("entropy: " + getH(frequencies, frequencies.get(frequencies.size() - 1)));
      //Math.log(x) / Math.log(2)
  }

  public static void textGenerator(int[] data)throws IOException{
    
    ArrayList<String> randomList = new ArrayList<String>();

    for (int i = 0 ; i < data.length; i++){

       for (int j = 0; j < data[i]; j++)
          randomList.add("" + (char)(i + 97));
    }
    System.out.println("testing array frequencies: " + randomList);

    //int rangepoint = letterFrequency[0]  if(rand >= 0 && rand < lF[0]) `

    //StringBuilder text = new StringBuilder();
    Random r = new Random();
    Writer output = new FileWriter("testText");
    OutputStream out = new FileOutputStream("testText");
    for (int i = 0; i < 10000; i++){

        //text.append(randomList.get(r.nextInt(randomList.size())));
        out.write(randomList.get(r.nextInt(randomList.size())).getBytes());
    }


   // output.write(text.toString());


  }

  public static int sum(int endpoint, int[] data){
    int sum = 0;
    for (int i = 0; i < endpoint; i++){
        sum += data[i];
    }
    return sum;
  }

  public static int[] getArray(ArrayList<Integer> list){

    int[] result = new int[list.size()];

    for (int i = 0;i < list.size();i++){

      result[i] = list.get(i);
    }
    return result;
  }

  public static ArrayList<Integer> getFrequencies(Scanner scan){
  
    ArrayList<Integer> data = new ArrayList<Integer>();

    int total = 0, temp=0;

    while (scan.hasNext()){
      temp = scan.nextInt();
      total+=temp;
      data.add(temp);
    }
    /*total count of characters is inserted at end of arrayList*/
    //data.add(total);
    System.out.println(data);

   
    return data;
  }

  public static double getH(ArrayList<Integer> data, int denominator){
      double h = 0;
      for (int i = 0; i < data.size() - 1; i++){
          int val = data.get(i);
          h+= (1.0*val/denominator) * (Math.log(val)/Math.log(2));
      }
      return h;


  }
}
