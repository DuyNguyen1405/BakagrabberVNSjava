package core;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Manga {

    private String title;
    private String associatedNames;
    private String description;
    private String groupsScanlating;
    private String status;
    private String genres;
    private String authors;
    private String artists;
    private String year;
    private String cover;
    private String lastestChapter;
    private String mangaupdatesLink;

    public String getTitle() {
        return title;
    }

    public String getAssociatedNames() {
        return associatedNames;
    }

    public String getDescription() {
        return description;
    }

    public String getGroupsScanlating() {
        return groupsScanlating;
    }

    public String getStatus() {
        return status;
    }

    public String getGenres() {
        return genres;
    }

    public String getAuthors() {
        return authors;
    }

    public String getArtists() {
        return artists;
    }

    public String getYear() {
        return year;
    }

    public String getCover() {
        return cover;
    }

    public String getLastestChapter() {
        return lastestChapter;
    }

    public String getMangaupdatesLink() {
        return mangaupdatesLink;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAssociatedNames(String associatedNames) {
        this.associatedNames = associatedNames;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGroupsScanlating(String groupsScanlating) {
        this.groupsScanlating = groupsScanlating;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public void setArtists(String artists) {
        this.artists = artists;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setLastestChapter(String lastestChapter) {
        this.lastestChapter = lastestChapter;
    }

    public void setMangaupdatesLink(String mangaupdatesLink) {
        this.mangaupdatesLink = mangaupdatesLink;
    }

    public Manga() {
        String UNDEFINED_ATTRIBUTE = "N/A";
        setTitle(UNDEFINED_ATTRIBUTE);
        setAssociatedNames(UNDEFINED_ATTRIBUTE);
        setDescription(UNDEFINED_ATTRIBUTE);
        setGroupsScanlating(UNDEFINED_ATTRIBUTE);
        setStatus(UNDEFINED_ATTRIBUTE);
        setGenres(UNDEFINED_ATTRIBUTE);
        setAuthors(UNDEFINED_ATTRIBUTE);
        setArtists(UNDEFINED_ATTRIBUTE);
        setYear(UNDEFINED_ATTRIBUTE);
        setCover(UNDEFINED_ATTRIBUTE);
        setLastestChapter(UNDEFINED_ATTRIBUTE);
        setMangaupdatesLink(UNDEFINED_ATTRIBUTE);
    }

    public Manga(String mangaupdatesLink) throws IOException {
        this();
        this.mangaupdatesLink = mangaupdatesLink;
        URL url = new URL(mangaupdatesLink);
        URLConnection uc = url.openConnection();
        uc.connect();
        BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream(), "windows-1256"));
        String line;

        while ((line = br.readLine()) != null) {
            String[] tempArray;
            StrBuilder sb = new StrBuilder();

            if (line.contains("You specified an invalid series id.")) {
                throw new MalformedURLException("You specified an invalid series id.");
            }

            if (line.contains("releasestitle tabletitle")) {
                sb.append(StringUtils.substringBetween(line, "\"releasestitle tabletitle\">", "</span>"));
                this.title = StringEscapeUtils.unescapeHtml4(sb.toString());
                sb.clear();
                continue;
            }

            if (line.contains("Description")) {
                line = br.readLine();
                if (!line.contains("N/A")) {
                    this.description = StringUtils.substringAfter(line, ">");
                    this.description = this.description.replaceAll("<BR>", "\n");

                    // Convert all embedded link
                    String embeddedLink = "<a href='(.*)' .*<u>(.*)</u>.*";
                    this.description = this.description.replaceAll(embeddedLink, "[URL=$1][U]$2[/U][/URL]");

                    // Convert all <x>x</x> to [x]x[/x]
                    String convertToBBcode = "<(.?)([^a])>";
                    this.description = this.description.replaceAll(convertToBBcode, "[$1$2]");

                    // Remove all strings start with "<" and end with ">"
                    String remove = "<(.|\n)*?>";
                    this.description = this.description.replaceAll(remove, "").trim();
                    this.description = StringEscapeUtils.unescapeHtml4(this.description);
                    sb.clear();
                }
            }

            if (line.contains("Associated Names")) {
                line = br.readLine();
                if (!line.contains("N/A")) {
                    tempArray = StringUtils.substringAfter(line, ">").split("<br />");
                    for (int i = 0; i < tempArray.length; i++)
                        tempArray[i] = StringEscapeUtils.unescapeHtml4(tempArray[i]);
                    this.associatedNames = StringUtils.join(tempArray, "\n");
                }
            }

            if (line.contains("Groups Scanlating")) {
                line = br.readLine();
                if (!line.contains("N/A")) {
                    tempArray = StringUtils.substringsBetween(line, "<u>", "</u>");
                    this.groupsScanlating = StringUtils.join(tempArray, ", ");
                }
            }

            if (line.contains("<b>Latest Release(s)</b>")) {
                line = br.readLine();
                if (!line.contains("N/A")) {
                    sb.append(StringUtils.substringBetween(line, "\" >", " by"));
                    sb.replaceAll("</i>", "");
                    sb.replaceAll("<i>", "");
                    this.lastestChapter = sb.toString();
                    sb.clear();
                }
            }

            if (line.contains("Status in Country of Origin")) {
                line = br.readLine();
                if (!line.contains("N/A")) {
                    tempArray = StringUtils.substringAfter(line, ">").split("<BR>");
                    this.status = StringUtils.join(tempArray, "\n");
                }
            }

            // get cover
            if (line.contains("https://www.mangaupdates.com/image/")) {
                this.cover = StringUtils.substringBetween(line, "src='", "'><");
            }

            if (line.contains("<b>Genre</b>")) {
                line = br.readLine();
                if (!line.contains("N/A")) {
                    tempArray = StringUtils.substringsBetween(line, "<u>", "</u></a>");
                    this.genres = StringUtils.join(tempArray, ", ");
                }
            }

            if (line.contains("<b>Author(s)</b>")) {
                String currentAuthor;
                line = br.readLine();
                if (!line.contains("N/A")) {
                    String[] temp = StringUtils.substringAfter(line, ">").split("<BR>");
                    tempArray = new String[temp.length];
                    for (int i = 0; i < temp.length; i++) {
                        if (temp[i].contains("add_author")) {
                            currentAuthor = StringUtils.substringBefore(temp[i], "&nbsp;");
                        } else {
                            currentAuthor = StringUtils.substringBetween(temp[i], "<u>", "</u></a>");
                        }
                        tempArray[i] = currentAuthor;
                    }

                    this.authors = StringUtils.join(tempArray, "\n");
                }
            }

            if (line.contains("<b>Artist(s)</b>")) {
                String currentArtist;
                line = br.readLine();
                if (!line.contains("N/A")) {
                    String[] temp = StringUtils.substringAfter(line, ">").split("<BR>");
                    tempArray = new String[temp.length];
                    for (int i = 0; i < temp.length; i++) {
                        if (temp[i].contains("add_author")) {
                            currentArtist = StringUtils.substringBefore(temp[i], "&nbsp;");
                        } else {
                            currentArtist = StringUtils.substringBetween(temp[i], "<u>", "</u></a>");
                        }
                        tempArray[i] = currentArtist;
                    }
                    this.artists = StringUtils.join(tempArray, "\n");
                }
            }

            if (line.contains("<b>Year</b>")) {
                line = br.readLine();
                if (!line.contains("N/A")) {
                    this.year = StringUtils.substringAfter(line, ">");
                    return;
                }
            }
        }
    }
}