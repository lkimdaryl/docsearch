import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

class FileHelpers {
    static List<File> getFiles(Path start) throws IOException {
        File f = start.toFile();
        List<File> result = new ArrayList<>();
        if(f.isDirectory()) {
            System.out.println("It's a folder");
            File[] paths = f.listFiles();
            for(File subFile: paths) {
                result.addAll(getFiles(subFile.toPath()));
            }
        }
        else {
            result.add(start.toFile());
        }
        return result;
    }
    static String readFile(File f) throws IOException {
        System.out.println(f.toString());
        return new String(Files.readAllBytes(f.toPath()));
    }
}

class Handler implements URLHandler {
    List<File> files;
    Handler(String directory) throws IOException {
      this.files = FileHelpers.getFiles(Paths.get(directory));
    }
    public String handleRequest(URI url) throws IOException {
        int numFiles = this.files.size();
        if (url.getPath().equals("/")){
            return "There are " + numFiles + " files to search";
        }else{
            if (url.getPath().contains("/search")) {
                // return "I'm searching";
                String[] parameters = url.getQuery().split("=");
                // return parameters[1];

                //search?q=search-term prints "There were NNNN files found:" and then
                // a list of all the paths of files that contain that search term. 
                // For example, if the search term is base pair it should print the same
                //  paths you found in your search above.

                String[] myFiles = new String[numFiles];

                for (int i=0; i<numFiles; i++ ){
                    myFiles[i] = files.get(i).toString();
                }
                                
                List<String> stringFiles = new ArrayList<>();
                for (int i=0; i<myFiles.length; i++ ){
                    if (myFiles[i].contains(parameters[1])){
                        stringFiles.add(myFiles[i]);
                    }
                } 

                int numStringFiles = stringFiles.size();
                String s = "There were " + Integer.toString(numStringFiles) + " found:\n";
                for (int i=0; i<numStringFiles; i++ ){
                    s = s + stringFiles.get(i) + "\n";
                } 
                return s;  
            }
        }        

      return "Don't know how to handle that path!";
    }
}

class DocSearchServer {
    public static void main(String[] args) throws IOException {
        if(args.length == 0){
            System.out.println("Missing port number! Try any number between 1024 to 49151");
            return;
        }

        int port = Integer.parseInt(args[0]);

        Server.start(port, new Handler("./technical/"));
    }
}

