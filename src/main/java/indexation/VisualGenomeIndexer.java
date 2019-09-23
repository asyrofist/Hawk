package indexation;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import edu.stanford.nlp.simple.Sentence;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;

import java.io.File;
import java.io.IOException;

/**
 * Indexes attributes.json file, from Visual Genome
 */
public class VisualGenomeIndexer {
    public static void indexGenomeAttributes(File source, Directory destination) throws IOException {
        IndexWriter writer;
        Analyzer analyzer = new DefinitionAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        try {
            writer = new IndexWriter(destination, config);
        } catch (IOException e) {
            System.err.println("Invalid destination");
            return;
        }

        JsonFactory jsonFactory = new JsonFactory();
        JsonParser parser = jsonFactory.createParser(source);
        parser.nextToken();

        Document currentDocument = new Document();
        Boolean isEmpty = true;
        int current_image_id = -1;
        int h = -1;
        int w = -1;
        int y = -1;
        int x = -1;
        while (parser.hasCurrentToken()) {
            if (parser.getText().equals("image_id") && parser.currentToken() == JsonToken.FIELD_NAME) {
                parser.nextToken();
                if (parser.currentToken() == JsonToken.VALUE_NUMBER_INT) {
                    current_image_id = parser.getIntValue();
                }
            } else if (parser.getText().equals("h") && parser.currentToken() == JsonToken.FIELD_NAME) {
                parser.nextToken();
                if (parser.currentToken() == JsonToken.VALUE_NUMBER_INT) {
                    h = parser.getIntValue();
                }
            } else if (parser.getText().equals("w") && parser.currentToken() == JsonToken.FIELD_NAME) {
                parser.nextToken();
                if (parser.currentToken() == JsonToken.VALUE_NUMBER_INT) {
                    w = parser.getIntValue();
                }
            } else if (parser.getText().equals("y") && parser.currentToken() == JsonToken.FIELD_NAME) {
                parser.nextToken();
                if (parser.currentToken() == JsonToken.VALUE_NUMBER_INT) {
                    y = parser.getIntValue();
                }
            } else if (parser.getText().equals("x") && parser.currentToken() == JsonToken.FIELD_NAME) {
                parser.nextToken();
                if (parser.currentToken() == JsonToken.VALUE_NUMBER_INT) {
                    x = parser.getIntValue();
                }
            } else if (parser.getText().equals("synsets") & parser.currentToken() == JsonToken.FIELD_NAME) {
                if (!isEmpty) {
                    if (current_image_id != -1) {
                        currentDocument.add(new StoredField("image_id", current_image_id));
                    }
                    if (h != -1) {
                        currentDocument.add(new StoredField("h", h));
                    }
                    if (w != -1) {
                        currentDocument.add(new StoredField("w", w));
                    }
                    if (y != -1) {
                        currentDocument.add(new StoredField("y", y));
                    }
                    if (x != -1) {
                        currentDocument.add(new StoredField("x", x));
                    }
                    writer.addDocument(currentDocument);
                    currentDocument = new Document();
                    isEmpty = true;
                }
                parser.nextToken();
                // Field name attributes either indicates an array of synset objects (thus the START_OBJECT check)
                // or an array of VALUE_STRINGS indicating attributes for a particular synset, which are of interest to us
            } else if (parser.getText().equals("attributes")) {
                parser.nextToken();
                if (parser.nextToken() != JsonToken.START_OBJECT) {
                    while (parser.currentToken() != JsonToken.END_ARRAY) {
                        if (parser.currentToken() == JsonToken.VALUE_STRING) {
                            if (parser.getText().trim().length() > 0) {
                                for (String lemmatizedAttribute : new Sentence(parser.getText()).lemmas()) {
                                    currentDocument.add(new TextField("attribute", lemmatizedAttribute, Field.Store.YES));
                                }
                                isEmpty = false;
                            }
                        }
                        parser.nextToken();
                    }
                }
            } else if (parser.getText().equals("names") & parser.currentToken() == JsonToken.FIELD_NAME) {
                while (parser.currentToken() != JsonToken.END_ARRAY) {
                    if (parser.currentToken() == JsonToken.VALUE_STRING) {
                        if (parser.getText().trim().length() > 0) {
                            for (String lemmatizedName : new Sentence(parser.getText()).lemmas()) {
                                currentDocument.add(new TextField("attribute", lemmatizedName, Field.Store.YES));
                            }
                            isEmpty = false;
                        }
                    }
                    parser.nextToken();
                }
            } else {
                parser.nextToken();
            }
        }
        parser.close();

        try {
            writer.close();
        } catch (IOException e) {
            System.err.println("Failed to close writer");
            e.printStackTrace();
        }

    }

    public static void indexGenomeRelationships(File source, Directory destination) throws IOException {
        IndexWriter writer;
        Analyzer analyzer = new DefinitionAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        try {
            writer = new IndexWriter(destination, config);
        } catch (IOException e) {
            System.err.println("Invalid destination");
            return;
        }

        JsonFactory jsonFactory = new JsonFactory();
        JsonParser parser = jsonFactory.createParser(source);
        parser.nextToken();

        Document currentDocument = new Document();
        Boolean isEmpty = true;
        int current_image_id = -1;
        int h = -1;
        int w = -1;
        int y = -1;
        int x = -1;
        while (parser.hasCurrentToken()) {
            if (parser.getText().equals("image_id") && parser.currentToken() == JsonToken.FIELD_NAME) {
                parser.nextToken();
                if (parser.currentToken() == JsonToken.VALUE_NUMBER_INT) {
                    current_image_id = parser.getIntValue();
                }
            } else if (parser.getText().equals("h") && parser.currentToken() == JsonToken.FIELD_NAME) {
                parser.nextToken();
                if (parser.currentToken() == JsonToken.VALUE_NUMBER_INT) {
                    h = parser.getIntValue();
                }
            } else if (parser.getText().equals("w") && parser.currentToken() == JsonToken.FIELD_NAME) {
                parser.nextToken();
                if (parser.currentToken() == JsonToken.VALUE_NUMBER_INT) {
                    w = parser.getIntValue();
                }
            } else if (parser.getText().equals("y") && parser.currentToken() == JsonToken.FIELD_NAME) {
                parser.nextToken();
                if (parser.currentToken() == JsonToken.VALUE_NUMBER_INT) {
                    y = parser.getIntValue();
                }
            } else if (parser.getText().equals("x") && parser.currentToken() == JsonToken.FIELD_NAME) {
                parser.nextToken();
                if (parser.currentToken() == JsonToken.VALUE_NUMBER_INT) {
                    x = parser.getIntValue();
                }
            } else if (parser.getText().equals("predicate") & parser.currentToken() == JsonToken.FIELD_NAME) {
                if (!isEmpty) {
                    if (current_image_id != -1) {
                        currentDocument.add(new StoredField("image_id", current_image_id));
                    }
                    if (h != -1) {
                        currentDocument.add(new StoredField("h", h));
                    }
                    if (w != -1) {
                        currentDocument.add(new StoredField("w", w));
                    }
                    if (y != -1) {
                        currentDocument.add(new StoredField("y", y));
                    }
                    if (x != -1) {
                        currentDocument.add(new StoredField("x", x));
                    }
                    writer.addDocument(currentDocument);
                    currentDocument = new Document();
                    isEmpty = true;
                }
                parser.nextToken();
                currentDocument.add(new TextField("relationship", parser.getText(), Field.Store.YES));
            } else if (parser.getText().equals("name")) {
                parser.nextToken();
                if (parser.currentToken() == JsonToken.VALUE_STRING) {
                    if (parser.getText().trim().length() > 0) {
                        for (String lemmatizedName : new Sentence(parser.getText()).lemmas()) {
                            currentDocument.add(new TextField("relationship", lemmatizedName, Field.Store.YES));
                        }
                        isEmpty = false;
                    }
                }
                parser.nextToken();
            } else if (parser.getText().equals("names") & parser.currentToken() == JsonToken.FIELD_NAME) {
                while (parser.nextToken() != JsonToken.END_ARRAY) {
                    if (parser.currentToken() == JsonToken.VALUE_STRING) {
                        if (parser.getText().trim().length() > 0) {
                            for (String lemmatizedName : new Sentence(parser.getText()).lemmas()) {
                                currentDocument.add(new TextField("relationship", lemmatizedName, Field.Store.YES));
                            }
                            isEmpty = false;
                        }
                    }
                }
            } else {
                parser.nextToken();
            }

        }

        try {
            writer.close();
        } catch (IOException e) {
            System.err.println("Failed to close writer");
            e.printStackTrace();
        }

        parser.close();

    }
}
