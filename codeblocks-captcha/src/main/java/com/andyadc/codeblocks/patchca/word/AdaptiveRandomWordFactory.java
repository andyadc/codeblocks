package com.andyadc.codeblocks.patchca.word;

import java.util.concurrent.ThreadLocalRandom;

public class AdaptiveRandomWordFactory extends RandomWordFactory {

    protected String wideCharacters;

    public AdaptiveRandomWordFactory() {
        characters = "ABCDEFGHKLMNPQRSTUVWXYabcdefghjkmnpstuvwxy23456789";
		wideCharacters = "mw";
    }

    public void setWideCharacters(String wideCharacters) {
        this.wideCharacters = wideCharacters;
    }

    @Override
    public String getNextWord() {
        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        StringBuilder sb = new StringBuilder();
        StringBuilder chars = new StringBuilder(characters);
        int l = minLength + (maxLength > minLength ? rnd.nextInt(maxLength - minLength) : 0);
        for (int i = 0; i < l; i++) {
            int j = rnd.nextInt(chars.length());
            char c = chars.charAt(j);
            if (wideCharacters.indexOf(c) != -1) {
                for (int k = 0; k < wideCharacters.length(); k++) {
                    int idx = chars.indexOf(String.valueOf(wideCharacters.charAt(k)));
                    if (idx != -1) {
                        chars.deleteCharAt(idx);
                    }
                }
            }
            sb.append(c);
        }
        return sb.toString();
    }
}
