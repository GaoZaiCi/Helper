/*
 * SkinSwitch - SkinRenderer
 * Copyright (C) 2014-2015  Baptiste Candellier
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.fmp.skins;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;

import java.util.HashMap;
import java.util.Map;

/**
 * Renders a skin in different ways. Create previews, like cropped heads or
 * full-sized back and front skin previews.
 *
 * @author outadoc
 */
public abstract class SkinRenderer {

    /**
     * Gets a cropped head from the skin.
     *
     * @param skin the skin to crop.
     * @return the head.
     */
    public static Bitmap getCroppedHead(Bitmap skin) {
        return Bitmap.createBitmap(skin, skin.getWidth() / 4, 0, skin.getWidth() / 2, skin.getWidth() / 2 - 1);
    }

    /**
     * Gets the preview of a skin from its bitmap.
     *
     * @param skin the bitmap of the skin.
     * @return the preview.
     */
    public static Bitmap getSkinPreview(Bitmap skin) {
        return getSkinPreview(skin, Side.FRONT);
    }

    /**
     * Gets the preview of a skin from its bitmap.
     *
     * @param skin the bitmap of the skin.
     * @param side the side for which to render it.
     * @return the preview.
     * @see Side
     */
    public static Bitmap getSkinPreview(Bitmap skin, Side side) {
        return getSkinPreview(skin, side, 6, Model.STEVE);
    }

    /**
     * Gets the preview of a skin from its bitmap.
     *
     * @param skin the bitmap of the skin.
     * @param side the side for which to render it.
     * @param zoom the scale factor for the preview.
     * @return the preview.
     * @see Side
     */
    public static Bitmap getSkinPreview(Bitmap skin, Side side, int zoom, Model model) {
        HashMap<BodyPart, Bitmap> skinBits = new HashMap<BodyPart, Bitmap>();
        HashMap<BodyPart, Bitmap> armorPieces = new HashMap<BodyPart, Bitmap>();

        final int armWidth = (model == Model.ALEX) ? 3 : 4;
        final boolean isNewFormat = isNewSkinFormat(skin);

        if (side == null || side == Side.FRONT) {
            // if we want a preview of the front of the skin or if nothing is
            // specified
            // get body parts, one at a time
            skinBits.put(BodyPart.HEAD, Bitmap.createBitmap(skin, 8, 8, 8, 8));
            skinBits.put(BodyPart.CHEST, Bitmap.createBitmap(skin, 20, 20, 8, 12));

            // if there's a specific skin for left arm, use it. else, flip the
            // right arm's skin and use it instead.
            skinBits.put(BodyPart.ARM_RIGHT, Bitmap.createBitmap(skin, 44, 20, armWidth, 12));
            skinBits.put(BodyPart.ARM_LEFT,
                    (!isNewFormat || areAllPixelsOfSameColor(Bitmap.createBitmap(skin, 36, 52, armWidth, 12)))
                            ? flipImage(skinBits.get(BodyPart.ARM_RIGHT)) : Bitmap.createBitmap(skin, 36, 52, armWidth, 12));

            // if there's a specific skin for left leg, use it. else, flip the
            // right leg's skin and use it instead.
            skinBits.put(BodyPart.LEG_RIGHT, Bitmap.createBitmap(skin, 4, 20, 4, 12));
            skinBits.put(BodyPart.LEG_LEFT,
                    (!isNewFormat || areAllPixelsOfSameColor(Bitmap.createBitmap(skin, 20, 52, 4, 12)))
                            ? flipImage(skinBits.get(BodyPart.LEG_RIGHT)) : Bitmap.createBitmap(skin, 20, 52, 4, 12));

            // it's armor time!
            armorPieces.put(BodyPart.HEAD, Bitmap.createBitmap(skin, 40, 8, 8, 8));

            if (isNewFormat) {
                armorPieces.put(BodyPart.CHEST, Bitmap.createBitmap(skin, 20, 36, 8, 12));

                armorPieces.put(BodyPart.ARM_LEFT, Bitmap.createBitmap(skin, 44, 36, armWidth, 12));
                armorPieces.put(BodyPart.ARM_RIGHT, Bitmap.createBitmap(skin, 52, 52, armWidth, 12));

                armorPieces.put(BodyPart.LEG_LEFT, Bitmap.createBitmap(skin, 4, 36, 4, 12));
                armorPieces.put(BodyPart.LEG_RIGHT, Bitmap.createBitmap(skin, 4, 52, 4, 12));
            } else {
                setEmptyArmor(armorPieces);
            }
        } else {
            // if we want a preview of the back of the skin
            skinBits.put(BodyPart.HEAD, Bitmap.createBitmap(skin, 24, 8, 8, 8));

            skinBits.put(BodyPart.CHEST, Bitmap.createBitmap(skin, 32, 20, 8, 12));

            skinBits.put(BodyPart.ARM_LEFT, Bitmap.createBitmap(skin, 48 + armWidth, 20, armWidth, 12));
            skinBits.put(BodyPart.ARM_RIGHT,
                    (!isNewFormat || areAllPixelsOfSameColor(Bitmap.createBitmap(skin, 40 + armWidth, 52, armWidth, 12)))
                            ? flipImage(skinBits.get(BodyPart.ARM_LEFT)) : Bitmap.createBitmap(skin, 40 + armWidth, 52,
                            armWidth, 12));

            skinBits.put(BodyPart.LEG_LEFT, Bitmap.createBitmap(skin, 12, 20, 4, 12));
            skinBits.put(BodyPart.LEG_RIGHT,
                    (!isNewFormat || areAllPixelsOfSameColor(Bitmap.createBitmap(skin, 28, 52, 4, 12)))
                            ? flipImage(skinBits.get(BodyPart.LEG_LEFT)) : Bitmap.createBitmap(skin, 28, 52, 4, 12));

            // it's armor time!
            armorPieces.put(BodyPart.HEAD, Bitmap.createBitmap(skin, 56, 8, 8, 8));

            if (isNewFormat) {
                armorPieces.put(BodyPart.CHEST, Bitmap.createBitmap(skin, 32, 36, 8, 12));

                armorPieces.put(BodyPart.ARM_LEFT, Bitmap.createBitmap(skin, 48 + armWidth, 36, armWidth, 12));
                armorPieces.put(BodyPart.ARM_RIGHT, Bitmap.createBitmap(skin, 56 + armWidth, 52, armWidth, 12));

                armorPieces.put(BodyPart.LEG_LEFT, Bitmap.createBitmap(skin, 12, 36, 4, 12));
                armorPieces.put(BodyPart.LEG_RIGHT, Bitmap.createBitmap(skin, 12, 52, 4, 12));
            } else {
                setEmptyArmor(armorPieces);
            }
        }

        Bitmap dest = Bitmap.createBitmap(16, 32, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(dest);

        HashMap<BodyPart, Bitmap> finalParts = new HashMap<BodyPart, Bitmap>();

        // at this point we most likely saturated the memory anyway, so.
        // MOAR. BITMAPS.
        finalParts.put(BodyPart.HEAD, overlayArmor(BodyPart.HEAD, skinBits, armorPieces));
        finalParts.put(BodyPart.CHEST, overlayArmor(BodyPart.CHEST, skinBits, armorPieces));
        finalParts.put(BodyPart.ARM_RIGHT, overlayArmor(BodyPart.ARM_RIGHT, skinBits, armorPieces));
        finalParts.put(BodyPart.ARM_LEFT, overlayArmor(BodyPart.ARM_LEFT, skinBits, armorPieces));
        finalParts.put(BodyPart.LEG_RIGHT, overlayArmor(BodyPart.LEG_RIGHT, skinBits, armorPieces));
        finalParts.put(BodyPart.LEG_LEFT, overlayArmor(BodyPart.LEG_LEFT, skinBits, armorPieces));

        // free all the bitmaps we can
        freeBitmapMap(skinBits);
        freeBitmapMap(armorPieces);

        // we got everything, just stick the parts where they belong on the
        // preview
        canvas.drawBitmap(finalParts.get(BodyPart.HEAD), getSrcRect(BodyPart.HEAD, finalParts),
                getDestRect(BodyPart.HEAD, finalParts, 4, 0), null);
        canvas.drawBitmap(finalParts.get(BodyPart.CHEST), getSrcRect(BodyPart.CHEST, finalParts),
                getDestRect(BodyPart.CHEST, finalParts, 4, 8), null);
        canvas.drawBitmap(finalParts.get(BodyPart.ARM_RIGHT), getSrcRect(BodyPart.ARM_RIGHT, finalParts),
                getDestRect(BodyPart.ARM_RIGHT, finalParts, 4 - armWidth, 8), null);
        canvas.drawBitmap(finalParts.get(BodyPart.ARM_LEFT), getSrcRect(BodyPart.ARM_LEFT, finalParts),
                getDestRect(BodyPart.ARM_LEFT, finalParts, 12, 8), null);
        canvas.drawBitmap(finalParts.get(BodyPart.LEG_RIGHT), getSrcRect(BodyPart.LEG_RIGHT, finalParts),
                getDestRect(BodyPart.LEG_RIGHT, finalParts, 4, 20), null);
        canvas.drawBitmap(finalParts.get(BodyPart.LEG_LEFT), getSrcRect(BodyPart.LEG_LEFT, finalParts),
                getDestRect(BodyPart.LEG_LEFT, finalParts, 8, 20), null);

        // free the last bitmaps
        freeBitmapMap(finalParts);
        return resizeImage(dest, zoom);
    }

    /**
     * Sets empty armor for all armor pieces.
     *
     * @param armorPieces the hashmap containing the armor pieces.
     */
    private static void setEmptyArmor(HashMap<BodyPart, Bitmap> armorPieces) {
        Bitmap empty = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);

        armorPieces.put(BodyPart.CHEST, empty);

        armorPieces.put(BodyPart.ARM_LEFT, empty);
        armorPieces.put(BodyPart.ARM_RIGHT, empty);

        armorPieces.put(BodyPart.LEG_LEFT, empty);
        armorPieces.put(BodyPart.LEG_RIGHT, empty);
    }

    /**
     * Checks if a skin is of the new format (square, armour for every body
     * part).
     *
     * @param skin the skin to check.
     * @return true if it's new, false if it's old.
     */
    private static boolean isNewSkinFormat(Bitmap skin) {
        return (skin.getHeight() == skin.getWidth() && skin.getWidth() == 64);
    }

    /**
     * Gets the bounds of a Bitmap.
     *
     * @param part the body part to retrieve the bounds of.
     * @param map  the hashmap that contains the bitmap to mesure.
     * @return the Rect dimensions of the bitmap.
     */
    private static Rect getSrcRect(BodyPart part, HashMap<BodyPart, Bitmap> map) {
        Bitmap img = map.get(part);
        return new Rect(0, 0, img.getWidth(), img.getHeight());
    }

    /**
     * Gets the destination bounds of a Bitmap.
     *
     * @param part the body part to retrieve the bounds of.
     * @param map  the hashmap that contains the bitmap to mesure.
     * @param x    the destination x axis.
     * @param y    the destination y axis.
     * @return the Rect destination dimensions of the bitmap.
     */
    private static Rect getDestRect(BodyPart part, HashMap<BodyPart, Bitmap> map, int x, int y) {
        Bitmap img = map.get(part);
        return new Rect(x, y, x + img.getWidth(), y + img.getHeight());
    }

    /**
     * Checks if all the pixels of a bitmap are of the same colour.
     *
     * @param image the bitmap.
     * @return true if they are, else false.
     */
    private static boolean areAllPixelsOfSameColor(Bitmap image) {
        // remember the color of the first pixel
        int firstPixColor = image.getPixel(0, 0);

        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                if (image.getPixel(j, i) != firstPixColor) {
                    return false;
                }
            }
        }

        // if all pixels are the same color, this should be true
        return true;
    }

    /**
     * Overlays the armour on a body part.
     *
     * @param which the part of the skin we have to overlay the armour on.
     * @return the combined armour and body part.
     */
    private static Bitmap overlayArmor(BodyPart which, HashMap<BodyPart, Bitmap> skinBits, HashMap<BodyPart,
            Bitmap> armorPieces) {
        Bitmap bodyPartBitmap = skinBits.get(which);
        Bitmap armorBitmap = armorPieces.get(which);

        Bitmap copy = bodyPartBitmap.copy(bodyPartBitmap.getConfig(), true);

        if (!areAllPixelsOfSameColor(armorBitmap)) {
            for (int i = 0; i < bodyPartBitmap.getHeight(); i++) {
                for (int j = 0; j < bodyPartBitmap.getWidth(); j++) {
                    if (Color.alpha(armorBitmap.getPixel(j, i)) == 255) {
                        copy.setPixel(j, i, armorBitmap.getPixel(j, i));
                    }
                }
            }
        }

        return copy;
    }

    /**
     * Recycles a whole Bitmap HashMap.
     *
     * @param map the hashmap to recycle.
     */
    private static void freeBitmapMap(HashMap<?, Bitmap> map) {
        for (Map.Entry<?, Bitmap> bitmapEntry : map.entrySet()) {
            Bitmap bmp = (Bitmap) (((Map.Entry) bitmapEntry)).getValue();
            bmp.recycle();
        }
    }

    /**
     * Flips a bitmap vertically (or horizontally, idek).
     *
     * @param image the image to flip.
     * @return the image flipped.
     */
    private static Bitmap flipImage(Bitmap image) {
        Matrix matrix = new Matrix();
        matrix.preScale(-1.0f, 1.0f);
        return Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);
    }

    /**
     * Resizes an image.
     *
     * @param image the image to resize.
     * @param zoom  the scale factor.
     * @return the resized image.
     */
    private static Bitmap resizeImage(Bitmap image, int zoom) {
        Bitmap resized = Bitmap.createScaledBitmap(image, image.getWidth() * zoom, image.getHeight() * zoom, false);
        image.recycle();
        return resized;
    }

    /**
     * Represents the front or the back of a skin.
     *
     * @author outadoc
     */
    public enum Side {
        FRONT, BACK
    }

    /**
     * Represents the different possible body parts of a skin.
     *
     * @author outadoc
     */
    private enum BodyPart {
        HEAD, CHEST, ARM_LEFT, ARM_RIGHT, LEG_LEFT, LEG_RIGHT
    }

    public enum Model {
        STEVE, ALEX
    }

}
