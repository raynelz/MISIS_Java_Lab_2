package org.example.task1;

import java.util.HashMap;
import java.util.Map;

interface YouTubeApiClient {
    Map<String, Video> popularVideos();
    Video getVideo(String videoId);
}

class Video {
    public final String id;
    public final String title;
    public final String data;

    public Video(String id, String title) {
        this.id = id;
        this.title = title;
        this.data = "Random video.";
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getData() {
        return data;
    }
}

class ThirdPartyYouTubeApiClient implements YouTubeApiClient {
    public HashMap<String, Video> popularVideos() {
        connectToServer("http://www/youtube.com");
        return getRandomVideos();
    }

    public Video getVideo(String videoId) {
        connectToServer("http://youtube.com/" + videoId);
        return getSomeVideo(videoId);
    }

    // -------------------------------------------------------------
    //MARK: - FAKE NETWORK METHODS

    private int random(int min, int max) {
        return min + (int) (Math.random() * ((max - min) + 1));
    }

    private void connectToServer(String server) {
        System.out.print("Connecting to" + server + "...");
        System.out.println("Connected!");
    }

    private HashMap<String, Video> getRandomVideos() {
        System.out.print("Downloading populars...");

        HashMap<String, Video> hmap = new HashMap<String, Video>();

        //MARK: - Заполняем Map фейк данными
        hmap.put("catzzzzzzzzz", new Video("sadgahasgdas", "Catzzzz.avi"));
        hmap.put("mkafksangasj", new Video("mkafksangasj", "Dog play with ball.mp4"));
        hmap.put("dancesvideoo", new Video("asdfas3ffasd", "Dancing video.mpq"));
        hmap.put("dlsdk5jfslaf", new Video("dlsdk5jfslaf", "Barcelona vs RealM.mov"));
        hmap.put("3sdfgsd1j333", new Video("3sdfgsd1j333", "Programing lesson#1.avi"));

        System.out.println("Done!");
        return hmap;
    }

    private Video getSomeVideo(String videoId) {
        System.out.print("Downloading video... ");

        Video video = new Video(videoId, "Some video title");

        System.out.println("Done!");
        return video;
    }
}

class CacheProxyYouTubeApiClient implements YouTubeApiClient {
    private YouTubeApiClient youtubeService;
    private Map<String, Video> cachePopular = new HashMap<String, Video>();
    private Map<String, Video> cacheAll = new HashMap<String, Video>();

    public CacheProxyYouTubeApiClient() {
        this.youtubeService = new ThirdPartyYouTubeApiClient();
    }

    public Map<String, Video> popularVideos() {
        if (cachePopular.isEmpty())
            cachePopular = youtubeService.popularVideos();
        else
            System.out.println("Retrieved list from cache.");

        return cachePopular;
    }

    public Video getVideo(String videoId) {
        Video video;
        if (!cacheAll.containsKey(videoId)) {
            video = youtubeService.getVideo(videoId);
            cacheAll.put(videoId, video);
        } else {
            System.out.println("Retrieved video '" + videoId + "' from cache.");
            video = cacheAll.get(videoId);
        }
        return video;
    }

    public void reset() {
        cachePopular.clear();
        cacheAll.clear();
    }
}

class YouTubeVideoDownloader {
    private YouTubeApiClient apiClient;

    public YouTubeVideoDownloader(YouTubeApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public void renderVideoPage(String videoId) {
        Video video = apiClient.getVideo(videoId);
        System.out.println("\n-------------------------------");
        System.out.println("Video page (imagine fancy HTML)");
        System.out.println("ID: " + video.getId());
        System.out.println("Title: " + video.getTitle());
        System.out.println("Video: " + video.getData());
        System.out.println("-------------------------------\n");
    }

    public void renderPopularVideos() {
        Map<String, Video> list = apiClient.popularVideos();
        System.out.println("\n-------------------------------");
        System.out.println("Most popular videos on YouTube (imagine fancy HTML)");
        for (Video video : list.values()) {
            System.out.println("ID: " + video.getId() + " / Title: " + video.getTitle());
        }
        System.out.println("-------------------------------\n");
    }
}

class ProxyService {
    public void exec() {
        YouTubeVideoDownloader nativeDownloader = new YouTubeVideoDownloader(new ThirdPartyYouTubeApiClient());
        YouTubeVideoDownloader smartDownloader = new YouTubeVideoDownloader(new ThirdPartyYouTubeApiClient());

        call(nativeDownloader);
        call(smartDownloader);
    }

    private void call(YouTubeVideoDownloader downloader) {
        //MARK: - User behavior in our app:
        downloader.renderPopularVideos();
        downloader.renderVideoPage("catzzzzzzzzz");
        downloader.renderPopularVideos();
        downloader.renderVideoPage("dancesvideoo");
        // Users might visit the same page quite often.
        downloader.renderVideoPage("catzzzzzzzzz");
        downloader.renderVideoPage("someothervid");
    }
}

public class Application {
    public static void main(String[] args) {
        ProxyService proxy = new ProxyService();
        proxy.exec();
    }
}
