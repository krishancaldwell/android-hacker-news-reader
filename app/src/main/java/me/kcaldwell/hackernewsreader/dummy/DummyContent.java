package me.kcaldwell.hackernewsreader.dummy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<DummyItem> ITEMS = new ArrayList<>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<Integer, DummyItem> ITEM_MAP = new HashMap<>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 0; i < COUNT; i++) {
            addItem(new DummyItem(i, false, DummyItem.Types.story.name(),
                    "K. Caldwell", System.currentTimeMillis(),
                    "This is a story", false, null,
                    null, "https://kcaldwell.me", i*i,
                    "Best Story Ever", 2*i));
        }

    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    /**
     * A dummy item representing a piece of content. API names in comments
     */
    public static class DummyItem {
        public final int id;
        public final boolean deleted;
        public final String type;
        public final String by;
        public final long time; // (in unix)
        public final String content; // text
        public final boolean dead;
        public final Integer parent;
        public final Integer[] children; // kids
        public final String url;
        public final int score;
        public final String title;
        public final int descendentsCount; // descendents

        public DummyItem(int id, boolean deleted, String type, String by, long time, String content, boolean dead, Integer parent, Integer[] children, String url, int score, String title, int descendentsCount) {
            this.id = id;
            this.deleted = deleted;
            this.type = type;
            this.by = by;
            this.time = time;
            this.content = content;
            this.dead = dead;
            this.parent = parent;
            this.children = children;
            this.url = url;
            this.score = score;
            this.title = title;
            this.descendentsCount = descendentsCount;
        }

        @Override
        public String toString() {
            return "DummyItem{" +
                    "id=" + id +
                    ", deleted=" + deleted +
                    ", type='" + type + '\'' +
                    ", by='" + by + '\'' +
                    ", time='" + time + '\'' +
                    ", content='" + content + '\'' +
                    ", dead=" + dead +
                    ", parent=" + parent +
                    ", children=" + Arrays.toString(children) +
                    ", url='" + url + '\'' +
                    ", score=" + score +
                    ", title='" + title + '\'' +
                    ", descendentsCount=" + descendentsCount +
                    '}';
        }

        public enum Types {
            job,
            story,
            comment,
            poll,
            pollopt
        }
    }
}
