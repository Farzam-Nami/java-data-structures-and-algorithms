package datastructures.applications;

import datastructures.linear.DynamicArray;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/** A playlist with unique track names and ordered, popularity and shuffled views. */
public final class MusicPlaylist implements Iterable<MusicPlaylist.Track> {
    /** Track identity and accumulated number of plays. */
    public static final class Track {
        private final String name, path;
        private int playCount;
        private Track(String name, String path) { this.name = name; this.path = path; }
        public String name() { return name; }
        public String path() { return path; }
        public int playCount() { return playCount; }
        @Override public String toString() { return name + " (" + playCount + " plays)"; }
    }

    private final DynamicArray<Track> tracks = new DynamicArray<>();
    public int size() { return tracks.size(); }
    public boolean isEmpty() { return tracks.isEmpty(); }
    /** Returns false rather than creating a duplicate track name. */
    public boolean add(String name, String path) {
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("name cannot be blank");
        if (path == null || path.trim().isEmpty()) throw new IllegalArgumentException("path cannot be blank");
        if (find(name) != null) return false;
        tracks.add(new Track(name, path)); return true;
    }
    public Track find(String name) {
        for (Track track : tracks) if (track.name.equals(name)) return track;
        return null;
    }
    /** A play changes only the count, not the insertion order. */
    public boolean play(String name) {
        Track track = find(name);
        if (track == null) return false;
        track.playCount++; return true;
    }
    public boolean remove(String name) {
        for (int i = 0; i < tracks.size(); i++) {
            if (tracks.get(i).name.equals(name)) { tracks.remove(i); return true; }
        }
        return false;
    }
    public Track get(int index) { return tracks.get(index); }
    public List<Track> byName() {
        List<Track> result = copy(); result.sort(Comparator.comparing(Track::name)); return result;
    }
    public List<Track> byPlayCount() {
        List<Track> result = copy();
        result.sort(Comparator.comparingInt(Track::playCount).reversed().thenComparing(Track::name));
        return result;
    }
    /** Returns a shuffled snapshot; a supplied seed makes examples reproducible. */
    public List<Track> shuffled(Random random) {
        Objects.requireNonNull(random, "random");
        List<Track> result = copy(); Collections.shuffle(result, random); return result;
    }
    private List<Track> copy() {
        List<Track> result = new ArrayList<>();
        for (Track track : tracks) result.add(track);
        return result;
    }
    @Override public Iterator<Track> iterator() { return copy().iterator(); }
}
