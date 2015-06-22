import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
import java.lang.Math;
public class Encoder{

  public static void main(String args[]) throws Exception{

      Scanner scan = new Scanner(new File(args[0]));

      ArrayList<Integer> frequencies = getFrequencies(scan);

      System.out.println("entropy: " + getH(frequencies, frequencies.get(frequencies.size() - 1)));
      //Math.log(x) / Math.log(2)
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
    data.add(total);
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
