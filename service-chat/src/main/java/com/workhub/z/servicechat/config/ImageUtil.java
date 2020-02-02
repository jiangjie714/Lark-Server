package com.workhub.z.servicechat.config;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
*@Description: 图片处理类
*@Author: 忠
*@date: 2019/5/16
*/
public abstract class ImageUtil {

    /**
     * 图片格式：JPG
     */
    private static final String PICTRUE_FORMATE = "jpg";

    /**
     * 生成组合头像
     * 画布宽度和高度为166，图片间距为2
     *
     * @param paths 用户头像路径列表
     * @param outPath  生成后的头像保存路径
     * @throws IOException
     */
    public static void generate(List<String> paths, String outPath) throws IOException {
        generate(paths, 166, 4, outPath);
    }

    /**
     * 生成组合头像
     *
     * @param paths    用户头像路径列表
     * @param length   画板的宽高和高度
     * @param interval 画板中的图片间距
     * @param outPath  生成后的头像保存路径
     * @throws IOException
     */
    public static void generate(List<String> paths, int length, int interval, String outPath) throws IOException {
        int wh = (length - interval * 4) / 3; // 每个图片的宽高和高度：图片数>4
        if (paths.size() == 1) {
            wh = length - interval * 2; // 每个图片的宽高和高度：图片数=1
        }
        if (paths.size() > 1 && paths.size() < 5) {
            wh = (length - interval * 3) / 2; // 每个图片的宽高和高度：图片数>0并且<5
        }
        List<BufferedImage> bufferedImages = new ArrayList<BufferedImage>();
        for (int i = 0; i < paths.size(); i++) {
            bufferedImages.add(ImageUtil.resize(paths.get(i), wh, wh, false));
        }
        // BufferedImage.TYPE_INT_RGB可以自己定义可查看API
        BufferedImage outImage = new BufferedImage(length, length, BufferedImage.TYPE_INT_RGB);
        // 生成画布
        Graphics g = outImage.getGraphics();
        Graphics2D g2d = (Graphics2D) g;
        // 设置背景色
        g2d.setBackground(new Color(100, 255, 255));
        // 通过使用当前绘图表面的背景色进行填充来清除指定的矩形。
        g2d.clearRect(0, 0, length, length);
        // 开始拼凑 根据图片的数量判断该生成那种样式的组合头像
        for (int i = 1; i <= bufferedImages.size(); i++) {
            int j = i % 3 + 1;
            if (bufferedImages.size() < 5) {
                j = i % 2 + 1;
            }
            int x = interval * j + wh * (j - 1);
            int split = (wh + interval) / 2;
            if (bufferedImages.size() == 9) {
                if (i <= 3) {
                    g2d.drawImage(bufferedImages.get(i - 1), x, wh * 2 + interval * 3, null);
                } else if (i <= 6) {
                    g2d.drawImage(bufferedImages.get(i - 1), x, wh + interval * 2, null);
                } else {
                    g2d.drawImage(bufferedImages.get(i - 1), x, interval, null);
                }
            } else if (bufferedImages.size() == 8) {
                if (i <= 3) {
                    g2d.drawImage(bufferedImages.get(i - 1), x, wh * 2 + interval * 3, null);
                } else if (i <= 6) {
                    g2d.drawImage(bufferedImages.get(i - 1), x, wh + interval * 2, null);
                } else {
                    g2d.drawImage(bufferedImages.get(i - 1), x - split, interval, null);
                }
            } else if (bufferedImages.size() == 7) {
                if (i <= 3) {
                    g2d.drawImage(bufferedImages.get(i - 1), x, wh * 2 + interval * 3, null);
                } else if (i <= 6) {
                    g2d.drawImage(bufferedImages.get(i - 1), x, wh + interval * 2, null);
                } else {
                    g2d.drawImage(bufferedImages.get(i - 1), (length - wh) / 2, interval, null);
                }
            } else if (bufferedImages.size() == 6) {
                if (i <= 3) {
                    g2d.drawImage(bufferedImages.get(i - 1), x, wh * 2 + interval * 3 - split, null);
                } else if (i <= 6) {
                    g2d.drawImage(bufferedImages.get(i - 1), x, wh + interval * 2 - split, null);
                }
            } else if (bufferedImages.size() == 5) {
                if (i <= 3) {
                    g2d.drawImage(bufferedImages.get(i - 1), x, wh * 2 + interval * 3 - split, null);
                } else {
                    g2d.drawImage(bufferedImages.get(i - 1), x - split, wh + interval * 2 - split, null);
                }
            } else if (bufferedImages.size() == 4) {
                if (i <= 2) {
                    g2d.drawImage(bufferedImages.get(i - 1), x, wh + interval * 2, null);
                } else {
                    g2d.drawImage(bufferedImages.get(i - 1), x, interval, null);
                }
            } else if (bufferedImages.size() == 3) {
                if (i <= 2) {
                    g2d.drawImage(bufferedImages.get(i - 1), x, wh + interval * 2, null);
                } else {
                    g2d.drawImage(bufferedImages.get(i - 1), x - split, interval, null);
                }
            } else if (bufferedImages.size() == 2) {
                g2d.drawImage(bufferedImages.get(i - 1), x, wh + interval * 2 - split, null);
            } else if (bufferedImages.size() == 1) {
                g2d.drawImage(bufferedImages.get(i - 1), interval, interval, null);
            }
            // 需要改变颜色的话在这里绘上颜色。可能会用到AlphaComposite类
        }
        ImageIO.write(outImage, PICTRUE_FORMATE, new File(outPath));
    }


    public static BufferedImage resize(String filePath, int width, int height) {
        try {
            BufferedImage bi = ImageIO.read(new File(filePath));
            bi.getSubimage(0, 0, width, height);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 图片缩放
     *
     * @param filePath 图片路径
     * @param height   高度
     * @param width    宽度
     * @param bb       比例不对时是否需要补白
     */
    public static BufferedImage resize(String filePath, int height, int width, boolean bb) {
        try {
            double ratio = 0; // 缩放比例
            File f = new File(filePath);
            BufferedImage bi = ImageIO.read(f);
            Image itemp = bi.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            // 计算比例
            if ((bi.getHeight() > height) || (bi.getWidth() > width)) {
                if (bi.getHeight() > bi.getWidth()) {
                    ratio = (new Integer(height)).doubleValue() / bi.getHeight();
                } else {
                    ratio = (new Integer(width)).doubleValue() / bi.getWidth();
                }
                AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(ratio, ratio), null);
                itemp = op.filter(bi, null);
            }
            if (bb) {
                BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                Graphics2D g = image.createGraphics();
                g.setColor(Color.white);
                g.fillRect(0, 0, width, height);
                if (width == itemp.getWidth(null)) {
                    g.drawImage(itemp, 0, (height - itemp.getHeight(null)) / 2, itemp.getWidth(null), itemp.getHeight(null), Color.white, null);
                } else {
                    g.drawImage(itemp, (width - itemp.getWidth(null)) / 2, 0, itemp.getWidth(null), itemp.getHeight(null), Color.white, null);
                }
                g.dispose();
                itemp = image;
            }
            return (BufferedImage) itemp;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

//    public static void main(String[] args) throws Exception {
//        ArrayList<String> picUrls = new ArrayList<>();
//        String pic = "C:\\Users\\Public\\Pictures\\Sample Pictures\\";
//        picUrls.add(pic + "Ch.jpg");
//        picUrls.add(pic + "Ch.jpg");
//        picUrls.add(pic + "Ch.jpg");
//        picUrls.add(pic + "Ch.jpg");
//        picUrls.add(pic + "Ch.jpg");
//        picUrls.add(pic + "Ch.jpg");
//        picUrls.add(pic + "Ch.jpg");
//        picUrls.add(pic + "Ch.jpg");
//        picUrls.add(pic + "Ch.jpg");
//
//
//        //2 调用工具类 生成九宫格 并保存在已有路径
//        ImageUtil.generate(picUrls, "C:\\Users\\Public\\Pictures\\Sample Pictures\\adad.jpg");
//    }

    /**
     * 按照原图自身进行压缩
     * @param  fileName  文件名称
     * @param  outFileName 输出名称
     */
    public static void reduceImg(String fileName, String outFileName) {
        try {
//    		System.out.println(fileName);
            File file = new File(fileName);// 读入文件
            if (!file.exists()) {
                return;
            }
            Image img = ImageIO.read(file);      // 构造Image对象
            int width = img.getWidth(null);    // 得到源图宽
            int height = img.getHeight(null);  // 得到源图长
            int h = 80;
            int w = 80;
            // SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的 优先级比速度高 生成的图片质量比较好 但速度慢
            BufferedImage image = new BufferedImage(w, h,BufferedImage.TYPE_INT_RGB );
            image.getGraphics().drawImage(img.getScaledInstance(w, h,  Image.SCALE_SMOOTH), 0, 0, null); // 绘制缩小后的图
//            tag.getGraphics().drawImage(src.getScaledInstance(widthdist, heightdist,  Image.SCALE_AREA_AVERAGING), 0, 0,  null);
            File destFile = new File(outFileName);
            FileOutputStream out = new FileOutputStream(destFile); // 输出到文件流
            // 可以正常实现bmp、png、gif转jpg
//            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
//            encoder.encode(image); // JPEG编码
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}