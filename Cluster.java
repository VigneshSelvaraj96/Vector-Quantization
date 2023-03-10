
//class that represents each cluster in the clustering algorithm with functions to add and remove pixels from the cluster and create map of quantized image
public class Cluster {
    //parameters of the cluster
    int pixelNumber;
    int id;
    int pixel1center;
    int pixel2center;
    int totalpixel1;
    int totalpixel2;

      //Init method which takes id of the cluster
      public Cluster(int id, int pixel1, int pixel2){
        this.id = id;
        this.pixel1center = pixel1;
        this.pixel2center = pixel2;
        this.totalpixel1 = pixel1;
        this.totalpixel2 = pixel2;
    }



    //Method to add new pixels and compute the neighbouring value averages
    void addVector(int pixel1, int pixel2){
        totalpixel1 = totalpixel1 + pixel1;
        totalpixel2 = totalpixel2 + pixel2;
        pixelNumber = pixelNumber + 1;
        this.pixel1center = totalpixel1/pixelNumber;
        this.pixel2center = totalpixel2/pixelNumber;
      //  System.out.println("added");
    }

    //Method to remove pixels and compute the neighbouring value averages
    void removeVector(int pixel1, int pixel2){
        totalpixel1 = totalpixel1 - pixel1;
        totalpixel2 = totalpixel2 - pixel2;
        pixelNumber = pixelNumber - 1;
        if(pixelNumber == 0)
            {
                this.pixel1center = 0;
                this.pixel2center = 0;
            }
        else{
            this.pixel1center = totalpixel1/pixelNumber;
            this.pixel2center = totalpixel2/pixelNumber;
        }
       // System.out.println("removed");
    }



    //Method to get id of the cluster
    int getId(){
        return id;
    }


    //Method to get pixel center value of the cluster
    int[] getclustercenter(){
        return new int[]{pixel1center, pixel2center};
    }

    //method to get distance between an input pixel and the cluster center
    double getDistance(int pixel1, int pixel2){
        double distance = Math.sqrt(Math.pow((pixel1 - pixel1center), 2) + Math.pow((pixel2 - pixel2center), 2));
        return distance;
    }
}
