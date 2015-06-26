import nayuki.huffmancoding.*;
import java.io.*;
import java.util.*;
import java.lang.*;

public class Encoder{

  public static void main(String args[]) throws Exception{

      Scanner scan = new Scanner(new File(args[0]));
      /*get frequencies from txt*/
      ArrayList<Integer> frequencies = getFrequencies(scan);
      int[] longFrequency = new int[257];
      for (int i= 0; i < frequencies.size(); i++){
        longFrequency[i+97]=frequencies.get(i);
      }
      /*calculate sum of all frequencies*/
      int _sum = sum(frequencies);
      /*turn arraylist into array*/
      int[] test = getArray(frequencies);
      /*entrop*/
      
      


      /*testing Huffman code found online*/
      FrequencyTable _table = new FrequencyTable(longFrequency);
      _table.increment(256);
      CodeTree tree = _table.buildCodeTree();

      textGenerator(test, "testText");
      //System.out.println("printing code tree: " + tree.toString());


      /*compress*/
      BitOutputStream bitOutput = new BitOutputStream(new FileOutputStream("testText.enc1"));
      HuffmanEncoder huffmanEncoder = new HuffmanEncoder(bitOutput);
      InputStream input = new FileInputStream("testText");
      compress(tree, input, bitOutput, huffmanEncoder);
      input.close();
      bitOutput.close();

      /*decompress*/
      BitInputStream input_Decode = new BitInputStream(new FileInputStream("testText.enc1"));
      OutputStream output_Decode = new FileOutputStream("testText.dec1");
      HuffmanDecoder decoder = new HuffmanDecoder(input_Decode);
      decompress(tree, input_Decode, output_Decode, decoder);
      input_Decode.close();
      output_Decode.close();

   //   System.out.println("test frequency table\n: " + _table.toString());
      System.out.println("Encoding 1: \n"+tree);
      System.out.println("entropy: " + getH(frequencies, _sum));
      System.out.println("bits/symbol: " + averageBits(tree, longFrequency,getArray(frequencies))+"\n");

      ArrayList<Integer> doubleAlph = new ArrayList<Integer>();
      /*double alphabet*/
      _sum = sum(frequencies);
      //System.out.println("sum: "+_sum);

      for(int i = 0; i < frequencies.size(); i++)
          for (int j = 0; j < frequencies.size(); j++)
              doubleAlph.add( (int)Math.floor((1.0*frequencies.get(i)/_sum) * (1.0*frequencies.get(j)/_sum)*100) );
          
      

      //System.out.println("getting double frequencies: " + doubleAlph);
      doubleTextGenerator(test, getArray(doubleAlph),"doubleTestText");

      /*compress*/
      BitOutputStream doubleBitOutput = new BitOutputStream(new FileOutputStream("doubleTestText.enc1"));
      HuffmanEncoder doubleHuffmanEncoder = new HuffmanEncoder(doubleBitOutput);
      InputStream doubleInput = new FileInputStream("doubleTestText");
      compress(tree, doubleInput, doubleBitOutput, doubleHuffmanEncoder);
      doubleInput.close();
      doubleBitOutput.close();

       /*decompress*/
      BitInputStream doubleInput_Decode = new BitInputStream(new FileInputStream("doubleTestText.enc1"));
      OutputStream doubleOutput_Decode = new FileOutputStream("doubleTestText.dec1");
      HuffmanDecoder doubleDecoder = new HuffmanDecoder(doubleInput_Decode);
      decompress(tree, doubleInput_Decode, doubleOutput_Decode, doubleDecoder);
      doubleInput_Decode.close();
      doubleOutput_Decode.close();
      //System.out.println(doubleAlph);
      System.out.println("Encoding 2: \n"+tree);
      System.out.println("2 Symbol entropy: " + getH(doubleAlph, sum(doubleAlph)));
      System.out.println("2 Symbol bits/symbol: " + averageBits(tree, longFrequency,getArray(doubleAlph)));

  }

  public static double averageBits(CodeTree tree, int[] frequencies, int[]freqCount){
      double sum = 0;
      int count = 0;
      for (int i = 0 ; i< frequencies.length;i++){

          if(frequencies[i]>0){
            sum+=tree.getCode(i).size();
            count++;
          }
      }
      return 1.0*sum/(freqCount.length);

  }

  public static void decompress(CodeTree codetree, BitInputStream input, OutputStream bitOutput
    ,HuffmanDecoder huffman)throws IOException{
    int symbol;
    huffman.codeTree=codetree;
    while((symbol = huffman.read()) != -1){
      //System.out.println("DEBUG: "+symbol);
      if (symbol == 256)
        break;
      bitOutput.write(symbol);
    }

  }

  public static void compress(CodeTree codetree, InputStream input, BitOutputStream bitOutput
    ,HuffmanEncoder huffman)throws IOException{
    int symbol;
    huffman.codeTree=codetree;
    while((symbol = input.read()) != -1){
      huffman.write(symbol);
    }
    huffman.write(256);
  }

  public static void doubleTextGenerator(int[] data, int[] other, String input)throws IOException{

      ArrayList<String> randomList = new ArrayList<String>();


    for (int i = 0 ; i < data.length; i++){
      for(int k = 0; k < data.length; k++){
         for (int j = 0; j < other[i]; j++)
            randomList.add("" + (char)(i + 97)+(char)(k+97));
          //System.out.println((char)(i + 97)+ " " + (char)(k+97));
      }
    }

    Random r = new Random();
    OutputStream out = new FileOutputStream(input);
    for (int i = 0; i < 10000; i++){
        out.write(randomList.get(r.nextInt(randomList.size())).getBytes());
    }
   


  }

  public static void textGenerator(int[] data, String input)throws IOException{
    
    ArrayList<String> randomList = new ArrayList<String>();

    for (int i = 0 ; i < data.length; i++){

       for (int j = 0; j < data[i]; j++)
          randomList.add("" + (char)(i + 97));
    }
//    System.out.println("testing array frequencies: " + randomList);

    Random r = new Random();
    OutputStream out = new FileOutputStream(input);
    for (int i = 0; i < 10000; i++){
        out.write(randomList.get(r.nextInt(randomList.size())).getBytes());
    }
  }

  public static int sum(ArrayList<Integer> data){
    int sum = 0;
    for (int i = 0; i < data.size(); i++){
        sum += data.get(i);
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

    int total = 0, symbol=0;

    while (scan.hasNext()){
      symbol = scan.nextInt();
      total+=symbol;
      data.add(symbol);
    }
    //System.out.println(data);

   
    return data;
  }

  public static double getH(ArrayList<Integer> data, int denominator){
      double h = 0;
      //System.out.println("denom: "+denominator);
      for (int i = 0; i < data.size() - 1; i++){
          int val = data.get(i);
          h+= (1.0*val/denominator)*(Math.log(val)/Math.log(2));
      }
      return h;


  }
}
