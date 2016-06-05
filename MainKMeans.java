package com.company;
import java.util.*;
import java.io.*;

public class MainKMeans
{
    static int N = 1000;
    static int K = 10;
    static double[][] data = new double[1000][5];
    static double[][] clusteredData = new double[1000][5];
    static double[][] randomPoints = new double[10][3];
    static double[][] prevCentroids = new double[10][3];
    static double[][] newCentroids = new double[10][3];
    static double MIN, MAX;

    public static void calculateMinMax()
    {
        MIN = MAX = data[0][0];
        for(int i=0; i<N; i++)
        {
            for(int j=1; j<4; j++)
            {
                if(data[i][j] < MIN)
                {
                    MIN = data[i][j];
                }
                if(data[i][j] > MAX)
                {
                    MAX = data[i][j];
                }
            }
        }
        //System.out.println("Min is " + MIN + "\nMax is " + MAX);
    }

    public static double randDouble(double min, double max) {
        Random rand= new Random();
        double randomNum = rand.nextDouble() * (max - min) + min;
        return randomNum;
    }

    public static void getRandomPoints()
    {
        for(int i=0; i<K; i++)
        {
            for(int j=0; j<3; j++)
            {
                randomPoints[i][j] = randDouble(MIN, MAX);
                prevCentroids[i][j] = randomPoints[i][j];
                newCentroids[i][j] = randomPoints[i][j];
            }
        }
        System.out.println("Random points: " + Arrays.deepToString(randomPoints) + "\n");
    }

    public static void clusterPoints()
    {
        double[] distances = new double[10];
        double min;
        int index=0;
        for(int i=0; i<N; i++)
        {
            for(int j=0; j<K; j++)
            {
                distances[j] =  Math.sqrt( (Math.pow(newCentroids[j][0]-clusteredData[i][1],2)) + (Math.pow(newCentroids[j][1]-clusteredData[i][2],2)) + (Math.pow(newCentroids[j][2]-clusteredData[i][3],2)) );
            }

            min = distances[0];
            for(int x=1; x<K; x++)
            {
                if(distances[x] < min)
                {
                    min = distances[x];
                    index = x;
                }
            }

            clusteredData[i][4] = index;

        }
    }

    public static void getCentroids()
    {
        double[][] sumForCentroid = new double[10][3];
        for(int i=0; i<10; i++)
        {
            for (int j=0; j<3; j++)
            {
                sumForCentroid[i][j] = 0;
                prevCentroids[i][j] = newCentroids[i][j];
            }
        }
        for (int i=0; i<N; i++)
        {
            for (int j=0; j<K; j++)
            {
                if (clusteredData[i][4] == j)
                {
                    sumForCentroid[j][0] = sumForCentroid[j][0] + clusteredData[i][1];
                    sumForCentroid[j][1] = sumForCentroid[j][1] + clusteredData[i][2];
                    sumForCentroid[j][2] = sumForCentroid[j][2] + clusteredData[i][3];
                }
            }
        }
        for(int i=0; i<10; i++)
        {
            newCentroids[i][0] = (sumForCentroid[i][0])/3;
            newCentroids[i][1] = (sumForCentroid[i][1])/3;
            newCentroids[i][2] = (sumForCentroid[i][2])/3;
        }
        System.out.println("Prev Centroids: " + Arrays.deepToString(prevCentroids));
        System.out.println("New Centroids:  " + Arrays.deepToString(newCentroids)+ "\n");
    }

    public static boolean areCentroidsSame()
    {
        //we compare prevCentroids with newCentroids, return true if they are same, return false otherwise

        boolean same = true;
        for(int i=0; i<10; i++)
        {
            for(int j=0; j<3; j++)
            {
                if(prevCentroids[i][j] != newCentroids[i][j])
                {
                    same = false;
                    break;
                }
            }
        }
        return same;
    }

    public static void main (String args[]) throws Exception {
        String filename = "1kpoints.txt";
        FileReader fr = new FileReader(filename);
        BufferedReader br = new BufferedReader(fr);

        String line;
        String[] split;
        for (int i = 0; i < N; i++)
        {
            line = br.readLine();
            split = line.split("\\s+");
            //System.out.println(Arrays.toString(split));
            for(int j=0; j<5; j++)
            {
                data[i][j] = Double.parseDouble(split[j]);
                //System.out.print(data[i][j] + "  ");
            }
            //System.out.println("");
        }
        fr.close();

        clusteredData = data;
        calculateMinMax();
        getRandomPoints();
        clusterPoints();
        //getCentroids();
        //clusterPoints();

        //while(!areCentroidsSame())
//        for(int i=0; i<5; i++)
//        {
//            clusterPoints();
//            getCentroids();
//        }

        String output = "kmeans_output.txt";
        FileWriter fw = new FileWriter(output);

        for(int i=0; i<N; i++)
        {
            for(int j=0; j<5; j++)
            {
                if(j==0 || j==4)
                {
                    fw.write(((int) clusteredData[i][j]) + "\t");
                }
                else
                {
                    fw.write(clusteredData[i][j] + "\t");
                }
                //System.out.print(clusteredData[i][j] + "  ");
            }
            fw.write("\n");
            //System.out.println("");
        }
        fw.close();

    }

}



