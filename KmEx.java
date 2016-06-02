import java.io.*;
import java.lang.*;
class KmEx
{
public static void main(String args[])
{
int N=10;
int data_array[]={1, 225,14,10,2,34,22,60,141,235};    // initial data
int i,a1,a2,x,y,n=0;
boolean flag=true;
float sum1=0,sum2=0;
x=data_array[0];y=data_array[1];
a1=x; a2=y;
int cluster1[]=new int[10],cluster2[]=new int[10];
for(i=0;i<10;i++)
    System.out.print(data_array[i]+ "\t");
System.out.println();

do
{
sum1=0;
sum2=0;
 n++;
 int k=0,j=0;
 for(i=0;i<10;i++)
 {
    if(Math.abs(data_array[i]-a1)<=Math.abs(data_array[i]-a2))
    {   cluster1[k]=data_array[i];
        k++;
    }
    else
    {   cluster2[j]=data_array[i];
        j++;
    }
 }
    System.out.println();
    for(i=0;i<10;i++)
        sum1=sum1+cluster1[i];
    for(i=0;i<10;i++)
        sum2=sum2+cluster2[i];
    x=a1;
    y=a2;
    a1=Math.round(sum1/k);
    a2=Math.round(sum2/j);
    if(a1==x && a2==y)
        flag=false;
    else
        flag=true;

    System.out.println("After iteration "+ n +" , cluster 1 :\n");    //printing the clusters of each iteration
    for(i=0;i<10;i++)
        System.out.print(cluster1[i]+ "\t");

    System.out.println("\n");
    System.out.println("After iteration "+ n +" , cluster 2 :\n");
    for(i=0;i<10;i++)
        System.out.print(cluster2[i]+ "\t");

}while(flag);

    System.out.println("Final cluster 1 :\n");            // final clusters
    for(i=0;i<10;i++)
        System.out.print(cluster1[i]+ "\t");

    System.out.println("\n");
    System.out.println("Final cluster 2 :\n");
    for(i=0;i<10;i++)
        System.out.print(cluster2[i]+ "\t");
 }
}
