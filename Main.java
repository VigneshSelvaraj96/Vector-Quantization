public class Main {

    //Main entry point of the application
    //All command line arguments are passed here
    public static void main(String[] args) {
        //Initialize the image display class used in Assignment 1
        ImageDisplay imgDisplay = new ImageDisplay();

        //Perform Vector Quantization
        VectorQuantization quantization = new VectorQuantization();
        VectorQuantization.clusterNumber = praseNumeberofCodeWords(args[2]);
        VectorQuantization.fileName = parseImageName(args[0]);
        int numvectors = Integer.parseInt(args[1]);

        //Process image
        quantization.ProcessImage(imgDisplay);
        //At the end show both images
        imgDisplay.showIms();
    }

    //Method to parse and validate image name
    private static String parseImageName(String imageName) {
        if (imageName != null){
            if (imageName.length() == 0){
                QuitProgram("Invalid file name");
            }else if (imageName.endsWith(".raw") || imageName.endsWith(".rgb")){
                return imageName;
            }
            else{
                QuitProgram("Invalid file name");
            }
        }else{
            QuitProgram("Invalid file name");
        }
        return "";
    }

    //Method to parse and validate number of clusters/codewords
    private static int praseNumeberofCodeWords(String parameter) {
        int N = Integer.parseInt(parameter);
        if (N <= 0){
            QuitProgram("Invalid number of clusters");
        }else if (!isPowerOfTwo(N)){
            QuitProgram("Number is not in powers of 2");
        }
        return N;
    }

    // Function to check if x is power of 2
    private static boolean isPowerOfTwo(int n)
    {
        if (n == 0) {
            return false;
        }
        while (n != 1)
        {
            if (n % 2 != 0) {
                return false;
            }
            n = n / 2;
        }
        return true;
    }


    //Reusable method to print the error message and quit the program
    public static void QuitProgram(String message){
        System.out.println(message);
        System.exit(1);
    }
}