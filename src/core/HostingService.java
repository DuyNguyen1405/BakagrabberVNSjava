package core;

/**
 * Created by skyderboob on 12/29/13.
 */
public enum HostingService {
    MEDIAFIRE("[IMG]http://i.imgur.com/2jn5MJB.png[/IMG]"),
    MEGAUPLOAD("[IMG]http://i.imgur.com/xRQajEg.jpg[/IMG]"),
    GOOGLE("[IMG]http://i.imgur.com/qeFVa0a.png[/IMG]"),
    BOX("[IMG]http://i.imgur.com/9kFwUnz.png[/IMG]");

    public final String imageLink;

    HostingService(String imageLink) {
        this.imageLink = imageLink;
    }
}
