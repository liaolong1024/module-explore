package com.liaolong.config;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ll
 * @since 2025-08-23 10:24
 */
public class T {
    public static void main(String[] args) {
        String s = new Solution().minWindow("aaaaaaaaaaaabbbbbcdd", "abcdd");
    }

    static class Solution {
        public String minWindow(String s, String t) {
            if (s == null || t == null || s.length() < t.length()) {
                return "";
            }
            Map<Character, Integer> smap = new HashMap<>();
            Map<Character, Integer> tmap = new HashMap<>();
            int l = 0, r = 0;
            while (r < t.length()) {
                tmap.put(t.charAt(r), tmap.getOrDefault(t.charAt(r), 0) + 1);
                smap.put(s.charAt(r), tmap.getOrDefault(s.charAt(r), 0) + 1);
                r += 1;
            }
            int[] ans = new int[] {0, s.length() + 1};
            while (l < r) {
                if (check(smap, tmap)) {
                    if (r - l < ans[1] - ans[0]) {
                        ans[0] = l;
                        ans[1] = r;
                    }
                    smap.put(s.charAt(l), smap.get(s.charAt(l)) - 1);
                    l += 1;
                } else {
                    if (r < s.length()) {
                        smap.put(s.charAt(r), smap.getOrDefault(s.charAt(r), 0) + 1);
                    } else {
                        break;
                    }
                    r += 1;
                }
            }
            return ans[1] <= s.length() ? s.substring(ans[0], ans[1]) : "";
        }

        public boolean check(Map<Character, Integer> smap, Map<Character, Integer> tmap) {
            for (Map.Entry<Character, Integer> entry: tmap.entrySet()) {
                if (!smap.containsKey(entry.getKey()) || smap.get(entry.getKey()).compareTo(entry.getValue()) < 0) {
                    return false;
                }
            }
            return true;
        }
    }
}
