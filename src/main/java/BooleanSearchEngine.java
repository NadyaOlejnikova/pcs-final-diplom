import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class BooleanSearchEngine implements SearchEngine {
    private String pdfName;
    private final Map<String, List<PageEntry>> pageEntryMap = new HashMap<>();


    public BooleanSearchEngine(File pdfsDir) throws IOException {
        // прочтите тут все pdf и сохраните нужные данные,
        // тк во время поиска сервер не должен уже читать файлы
        for (File file : pdfsDir.listFiles()) {
            pdfName = file.getName();
            PdfDocument pdfDocument = new PdfDocument(new PdfReader(file));
            int pages = pdfDocument.getNumberOfPages();
            for (int i = 1; i <= pages; i++) {

                String text = PdfTextExtractor.getTextFromPage(pdfDocument.getPage(i));
                String[] words = text.split("\\P{IsAlphabetic}+");
                List<String> sort = Arrays.stream(words)
                        .map(String::toLowerCase)
                        .sorted()
                        .collect(Collectors.toList());
                int count = -1;
                String key = sort.get(0);
                for (int j = 0; j < sort.size(); j++) {
                    count++;
                    if (sort.get(j).equals(key)) {
                        if (j == sort.size() - 1) {
                            count++;
                            pageMapPut(i, count, key);
                        }
                        continue;
                    } else {
                        pageMapPut(i, count, key);
                        key = sort.get(j);
                    }
                    count = 0;
                }
            }
        }
    }

    @Override
    public List<PageEntry> search(String word) {
        // тут реализуйте поиск по слову
        List<PageEntry> searchWord = pageEntryMap.get(word.toLowerCase());
        Collections.sort(searchWord);
        if (searchWord == null) {
            System.out.println("такого слова нет");
        }
        return searchWord;
    }

    public Map<String, List<PageEntry>> pageMapPut(int page, int count, String key) {
        PageEntry pageEntry = new PageEntry(pdfName, page, count);
        List<PageEntry> pageEntryList;
        if (pageEntryMap.containsKey(key)) {
            pageEntryList = pageEntryMap.get(key);

        } else {
            pageEntryList = new ArrayList<>();
        }
        pageEntryList.add(pageEntry);
        pageEntryMap.put(key, pageEntryList);
        return pageEntryMap;
    }

}

