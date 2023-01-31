import java.io.*;  // Import the File class
import java.util.Scanner;
  
public class DijkstraProtocol {  

    // Main method  
    public static void main(String argvs[]) { 
        
        // Clear messagefile.txt
        try {
            PrintWriter out = new PrintWriter("messagefile.txt");
            out.write(""); 
            out.close();
        } catch (IOException e) {
            System.out.print(e.getMessage());
        }

        // Create Array from the topofile.txt
        // Adding -1 to each edge to represent infinity
        int[][] model= new int[totalVertex][totalVertex];
        for(int i = 0; i < totalVertex; i++){
            for(int j = 0 ; j < totalVertex; j++) {   
                model[i][j]= -1;
            }
        }

        // Add Edge Values to Model
        try {
            File myFile = new File("topofile.txt");
            Scanner readFile = new Scanner(myFile);
            while (readFile.hasNextLine()) {
                String data = readFile.nextLine();
                String[] words = data.split(" ");
                int num1 = Integer.parseInt(words[0]);
                int num2 = Integer.parseInt(words[1]);
                int num3 = Integer.parseInt(words[2]);
                model[num1][num2] = num3;
                model[num2][num1] = num3;
            }
            readFile.close();

        } catch (FileNotFoundException x) {
            System.out.println("An error occurred.");
            x.printStackTrace();
        }

        //Run Dijkstra Algorithm
        DijkstraProtocol obj = new DijkstraProtocol();  
        obj.dijkstra(model); 
        
        // Add Changes to the Model and add the changes to the messagefile.txt
        // NOTE: DOES NOT ADD AN EXTRA NODE TO THE GRAPH!!!
        try {
            File myFile = new File("changesfile.txt");
            Scanner readFile = new Scanner(myFile);
            int counter = 1;
            while (readFile.hasNextLine()) {
                String data = readFile.nextLine();
                String[] words = data.split(" ");
                System.out.println("NewChange: ");
                int num1 = Integer.parseInt(words[0]);
                int num2 = Integer.parseInt(words[1]);
                int num3 = Integer.parseInt(words[2]);
                model[num1][num2] = num3;
                model[num2][num1] = num3;
                try {
                    PrintWriter out = new PrintWriter(new FileWriter("messagefile.txt", true));
                    out.write("New Change #" + counter + ":\n"); 
                    out.close();
                } catch (IOException e) {
                    System.out.print(e.getMessage());
                }
                obj.dijkstra(model);  
                counter++;
            }
                readFile.close();

            } catch (FileNotFoundException x) {
                System.out.println("An error occurred.");
                x.printStackTrace();
            }     
        }  

        static final int totalVertex = totalVertex();

    //Find the total amount of nodes/vertexes from the topofile.txt
    static int totalVertex() {
        int totalNodes = 0;
        try {
            File myFile = new File("topofile.txt");
            Scanner readFile = new Scanner(myFile);
            while (readFile.hasNextLine()) {
                String data = readFile.nextLine();
                String[] words = data.split(" ");
                int num1 = Integer.parseInt(words[0]);
                int num2 = Integer.parseInt(words[1]);
                if (num1 > totalNodes){
                    totalNodes = num1;
                    }   
                if(num2 > totalNodes) {
                        totalNodes = num2;
                    }
            }
            readFile.close();
            totalNodes = totalNodes + 1;
        } catch (FileNotFoundException x) {
            System.out.println("An error occurred.");
            x.printStackTrace();
        }
            return totalNodes;
    } 

    //Dijkstra Algorithm
    void dijkstra(int model[][]) {  
        for(int i = 0; i < 5; i++){
            int distance[] = new int[totalVertex]; 
            Boolean set[] = new Boolean[totalVertex]; 
            for (int j = 0; j < totalVertex; j++) {  
                distance[j] = Integer.MAX_VALUE;  
                set[j] = false;  
            }  

            distance[i] = 0;  
        
            for (int cnt = 0; cnt < totalVertex - 1; cnt++) {  
                int m = minimumDistance(distance, set);  
                set[m] = true;  
                for (int n = 0; n < totalVertex; n++)  
                    if (!set[n] && model[m][n] != -1 && distance[m] != Integer.MAX_VALUE && distance[m] + model[m][n] < distance[n]) {  
                        distance[n] = distance[m] + model[m][n]; 
                    }  
            }  
        
            printSolution(distance, totalVertex, i);  
            try {
                PrintWriter out = new PrintWriter(new FileWriter("messagefile.txt", true));
                out.write("\n"); 
                out.close();
            }
            catch (IOException e) {
                System.out.print(e.getMessage());
            }   
        }
    }  
    
    int minimumDistance(int distance[], Boolean set[])  {  
        int m = Integer.MAX_VALUE, m_index = -1;  
    
        for (int i = 0; i < totalVertex; i++) {  
            if (set[i] == false && distance[i] <= m) {  
                m = distance[i];  
                m_index = i;  
            }  
        }  
        return m_index;    
    } 

    // Write the shortest distance for each node to all other known nodes
    // Could not figure out the message part, but was able to add the topology entires for each node
    void printSolution(int distance[], int n, int s) {  
        try {
            PrintWriter out = new PrintWriter(new FileWriter("messagefile.txt", true));
            for (int j = 0; j < n; j++)  {
                out.write(s + " " + j + " " + distance[j] + "\n"); 
            }  
            out.close();
        }
        catch (IOException e) {
            System.out.print(e.getMessage());
        }   
    }
}  
