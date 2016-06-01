
import MPI.*;
import java.util.Random;

ipublic class MontePi {
        public static void main(String[] args) throws MPIException

        {
                
                MPI.Init(args);
                double x,y;
                double radius;
                double pi;
                int myrank;
                int size;
                int proc;
                int master = 0;
                int tag = 123;
                
                int myrank = MPI.COMM_WORLD.getRank() ;
                int size = MPI.COMM_WORLD.getSize() ;
                
                public static float randFloat(float min, float max) {
                        Random rand= new Random();
                        float randomNum = rand.nextFloat() * (max - min) + min;
                        return randomNum;
                }
                
                count = 0L;
                count_inside = 0L;
                radius = 5L;
                for (long i = 0L; i<100000; i++) {
                        
                        double x = randFloat(-5,5);
                        double y = randFloat(-5,5);
                        if (x*x + y*y < radius * radius){
                                 ++ count_inside;
                        }
                        count++;
                }
        
        
        if (myrank <1) {
                
                System.out.println(" Usage: MontePi number_of_iterations \n"
                MPI.Finalize();
                exit(-1);
                }
        
        if (myrank = 0) {
                for (proc=1; proc<size ; proc++) {
                        MPI Recv(count_inside, 1 ,MPI_REAL, proc, tag, MPI_COMMWORLD, status) ;
                        count_inside++;
                        }
                System.out.println("count_inside=" +count_inside);
                System.out.println("count" +count);
                pi = 4 * count_inside/count;
                System.out.println("pi=" +pi);
                }
        }

        else {

                System.out.println("Processor" +myrank "sending results" +count_inside "to master process")
                MPI Send(count_inside , 1 ,MPI_REAL, master , tag ,MPI_COMMWORLD) ;

        }

        MPI_Finalize();

}

                                                                                                                                                                 1,1           Top
