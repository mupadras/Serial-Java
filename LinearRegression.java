import java.util.*;
import java.io.*;


public class LinearRegression {

        public static void main(String[]args) {

               
                int n=1000;
                double []x = new double[n];
                double []y = new double[n];


                double sumx = 0.0; double sumy = 0.0; double sumz=0.0;
                for (n=0; n<n; n++) {
                        sumx  = sumx +  x[n];
                        sumz = sumz + (x[n] * x[n]);
                        sumy  = sumy + y[n];

                }

                double xAvg = sumx / n;
                double yAvg = sumy / n;
                double xX = 0.0; double yY = 0.0; double xY = 0.0;
                for (int i = 0; i < n; i++) {
                        xX += (x[i] - xAvg) * (x[i] - xAvg);
                        yY += (y[i] - yAvg) * (y[i] - yAvg);
                        xY += (x[i] - xAvg) * (y[i] - yAvg);

                }

                double beta1 = xY / xX;
                double beta0 = yAvg - beta1 * xAvg;

                System.out.println("y   = " + beta1 + " * x + " + beta0);


                int df = n - 1;
                double rss = 0.0;
                double ssr = 0.0;

                for (int i = 0; i < n; i++) {

                        double fit = beta1*x[i] + beta0;
                        rss += (fit - y[i]) * (fit - y[i]);
                        ssr += (fit - yAvg) * (fit - yAvg);

                }

                double R2    = ssr / yY;
                double svar  = rss / df;
                double svar1 = svar / xX;
                double svar0 = svar/n + xAvg*xAvg*svar1;
                System.out.println("R^2                 = " + R2);
                 System.out.println("std error of beta_1 = " + Math.sqrt(svar1));
                System.out.println("std error of beta_0 = " + Math.sqrt(svar0));
                svar0 = svar * sumz / (n * xX);
                System.out.println("std error of beta_0 = " + Math.sqrt(svar0));
                System.out.println("SSTO = " + yY);
                System.out.println("SSE  = " + rss);
                System.out.println("SSR  = " + ssr);
        }


}
