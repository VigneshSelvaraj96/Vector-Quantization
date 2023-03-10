import java.awt.image.*;
import java.io.*;
import java.util.*;


public class VectorQuantization {
     //Image Parameters
     static int imageWidth = 352;
     static int imageHeight = 288;

     // 2 pixel vector space
    static int vectorx = 255;
    static int vectory = 255;


     //Command line params
    static String fileName;
    static Cluster[] clusters;
    static int clusterNumber;


    //Image frames for original and processed images
    static BufferedImage img1;
    static BufferedImage img2;

    public void createCulsters(BufferedImage image){
        clusters = new Cluster[clusterNumber];
        int x = 0;
        int y = 0;
        int dx = vectorx/clusterNumber;
        int dy = vectory/clusterNumber;
        for (int i = 0; i < clusterNumber; i++) {
            clusters[i] = new Cluster(i, x, y);
            x = x + (dx);
            y = y + (dy);
        }
    }

    // Method to create the quantized image
    public void ProcessImage(ImageDisplay imgDisplay){
        imgDisplay.PrepareImages();
        try {
            //Input for image file
            File imageFile = new File(fileName);
            InputStream fileStream = new FileInputStream(imageFile);
            long len = imageFile.length();
            byte[] bytes = new byte[(int)len];
            int offset = 0;
            int numberRead;
            int index = 0;
            while (offset < bytes.length && (numberRead=fileStream.read(bytes, offset, bytes.length-offset)) >= 0){
                offset = offset + numberRead;
            }
            //Read the image file
            for (int y = 0; y < imageHeight; y++) {
                for (int x = 0; x < imageWidth; x++) {
                    byte r = bytes[index];
                    byte g = bytes[index];
                    byte b = bytes[index];
                    int pixel = 0Xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff) << 0;
                 //   System.out.println("readpixel");
                  //  System.out.println(pixel);
                    img1.setRGB(x,y,pixel);
                    index = index + 1;
                }
            }
            //Close the image file
            fileStream.close();
        }catch (Exception exception) {
            exception.printStackTrace();
        }

        //Computer vector space as defined in the assignment question
        int[][] vectorSpace = determineVectorSpace(VectorQuantization.img1);

        //Create the new image using the vector space mapping
        for (int y = 0; y < imageHeight; y++) {
            for (int x = 0; x < imageWidth; x+=2) {
                int clusterid = vectorSpace[y][x];
                int pixel1 = clusters[clusterid].getclustercenter()[0];
                pixel1 = (pixel1 << 16) | (pixel1 << 8) | pixel1;
                int pixel2 = clusters[clusterid].getclustercenter()[1];
                pixel2 = (pixel2 << 16) | (pixel2 << 8) | pixel2;
                img2.setRGB(x, y, pixel1);
                img2.setRGB(x+1, y, pixel2);
              //  System.out.println("writepixel");
              //  System.out.println(pixel1);
            }
        }
    }

    //Method to calculate the vector space
    public int[][] determineVectorSpace(BufferedImage image){
        createCulsters(image);
       // int[][] vectorSpace = new int[vectorx][vectory];
        // codebook for each pixel
        int[][] assignedcluster = new int[imageHeight][imageWidth];
        // fill the vector space with -1
        for (int i = 0; i < imageHeight; i++) {
            for (int j = 0; j < imageWidth; j++) {
                assignedcluster[i][j] = -1;
            }
        }
        //init all values to -1
       // Arrays.fill(vectorSpace,0);
       // Arrays.fill(assignedcluster,-1);
        boolean shouldRefine = true;
        while(shouldRefine){
            shouldRefine = false;
            System.out.println("Refining");
            // read every 2 adjacent pixels
            for (int y = 0; y < imageHeight; y++) {
                for (int x = 0; x < imageWidth; x+=2) {
                     //adjacent pixel pair - 2 pixels
                        int pixel1 = image.getRGB(x, y);
                        int pixel2 = image.getRGB(x+1, y);
                        //extract the grey scale value from the pixel
                        pixel1 = (pixel1 >> 16) & 0xff;
                        pixel1= Math.min(255, Math.max(0, pixel1));

                        pixel2 = (pixel2 >> 16) & 0xff;
                        pixel2= Math.min(255, Math.max(0, pixel2));
                        
                        Cluster nearestcluster = nearestCluster(pixel1,pixel2);
                        if (nearestcluster.getId() != assignedcluster[y][x]) {     //if the pixel is not in the same cluster
                            if (-1 != assignedcluster[y][x]) {              //if the pixel was in some other cluster remove it
                                clusters[assignedcluster[y][x]].removeVector(pixel1,pixel2);
                            }
                            nearestcluster.addVector(pixel1,pixel2);
                            assignedcluster[y][x] = nearestcluster.getId();
                            shouldRefine = true;
                        }
                    }
                }
            }
        return assignedcluster;
    }

    //Returns the nearest cluster
    public Cluster nearestCluster(int pixel1, int pixel2){
        Cluster cluster = null;
        double min = Double.MAX_VALUE;
        for (int i = 0; i < clusters.length; i++) {
            Double distance = clusters[i].getDistance(pixel1,pixel2);
        //    System.out.println("Distance is " + distance + " for cluster " + clusters[i].getId());
            if (min > distance){
                min = distance;
                cluster = clusters[i];
            }
        }
    //    System.out.println("Nearest cluster is " + cluster.getId());
        return cluster;
    }
}
