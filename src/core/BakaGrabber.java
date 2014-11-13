package core;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrBuilder;

import java.io.IOException;

public class BakaGrabber {

    private Manga manga;
    private boolean coverAdded = false;
    private String downloadLinks = "Insert download links here";

    public BakaGrabber(String mangaupdatesLink) throws IOException {
        manga = new Manga(mangaupdatesLink);
    }

    public static void main(String[] args) throws Exception {

        String url = "http://www.mangaupdates.com/series.html?id=86647";
        BakaGrabber test = new BakaGrabber(url);
        System.out.println(test.toString());
    }

    public String getCover() {
        return manga.getCover();
    }

    public void setCover(String coverLink) {
        coverAdded = true;
        manga.setCover(coverLink);
    }

    public void setdownloadLinks(String downloadLinks) {
        this.downloadLinks = downloadLinks;
    }

    @Override
    public String toString() {
        boolean isAdult = false;
        StrBuilder result = new StrBuilder();
        try {
            result = new StrBuilder(BBCodeLayout.getCurrentLayout());
        } catch (IOException e) {
            e.printStackTrace();
        }

        result = result.replaceAll("$title", manga.getTitle());
        result = result.replaceAll("$description", manga.getDescription());
        result = result.replaceAll("$associatedNames", manga.getAssociatedNames());
        result = result.replaceAll("$status", manga.getStatus());

        String[] genres = StringUtils.split(manga.getGenres(), ", ");
        for (int i = 0; i < genres.length; i++) {
            String cautiousGenres = "Adult|Horror|Mature|Smut";
            if (genres[i].matches(cautiousGenres)) {
                isAdult = true;
                genres[i] = "[COLOR=Red][B]" + genres[i] + "[/B][/COLOR]";
            }
        }
        result = result.replaceAll("$genres", StringUtils.join(genres, ", "));

        result = result.replaceAll("$authors", manga.getAuthors());
        result = result.replaceAll("$artists", manga.getArtists());
        result = result.replaceAll("$year", manga.getYear());

        if (coverAdded) {
            result = result.replaceAll("$cover", manga.getCover());
        } else {
            result = result.replaceAll("$cover", "You need to add cover manually yourself");
        }

        result = result.replaceAll("$lastestChapter", manga.getLastestChapter());
        result = result.replaceAll("$groupsScanlating", manga.getGroupsScanlating());
        result = result.replaceAll("$url", manga.getMangaupdatesLink());

        if (isAdult) {
            result = result.replaceAll("$downloadLinks", "[HIDE=0]" + downloadLinks + "[/HIDE]");
        } else {
            result = result.replaceAll("$downloadLinks", downloadLinks);
        }
        return result.toString();
    }
}
