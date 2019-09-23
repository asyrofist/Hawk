package examples;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

import static analysis.DictionaryClassifiers.vgRelationshipVote;
import static analysis.DictionaryClassifiers.visualGenomeVote;
import static indexation.VisualGenomeIndexer.indexGenomeAttributes;
import static indexation.VisualGenomeIndexer.indexGenomeRelationships;

public class LemmatizedVGRelationships {
        public static void main(String args[]) throws IOException {
        // creates a Lucene index from lemmatized WN graph
        String indexLocation = "src/main/resources/explainableVGRelationships";
        Directory indexDir = FSDirectory.open(Paths.get(indexLocation));
        File source = new File("src/main/resources/VGrelationships.json");
        indexGenomeRelationships(source, indexDir);

        // evaluates the index against sample data
        FileWriter resultWriter = new FileWriter("src/main/resources/VGRelationship.results");
        Scanner taskScanner = new Scanner(new File("src/main/resources/truth.txt"));
        while (taskScanner.hasNext()) {
            String[] currentLine = taskScanner.nextLine().split(",");
            int result = vgRelationshipVote(currentLine[0], currentLine[1], currentLine[2], indexLocation);
            resultWriter.write(currentLine[0] + "," + currentLine[1] + "," + currentLine[2] + "," + result + "\n");
        }
        taskScanner.close();
        resultWriter.close();
    }
}
