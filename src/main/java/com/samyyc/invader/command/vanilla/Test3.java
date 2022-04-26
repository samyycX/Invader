package com.samyyc.invader.command.vanilla;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.samyyc.invader.gun.packet.GamePacket;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.adventure.provider.MinestomPlainTextComponentSerializerProvider;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentBoolean;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.metadata.other.ItemFrameMeta;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.metadata.MapMeta;
import net.minestom.server.map.framebuffers.LargeDirectFramebuffer;
import net.minestom.server.map.framebuffers.LargeGraphics2DFramebuffer;
import net.minestom.server.tag.Tag;
import net.minestom.server.timer.TaskSchedule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.time.Duration;
import java.util.Set;
import java.util.stream.Collectors;

@TestOnly
public class Test3 extends Command {
    public Test3() {
        super("refresh");

        String url = "https://api.nyan.xyz/httpapi/sexphoto";

        ArgumentBoolean r18 = new ArgumentBoolean("r18");

        addSyntax((sender, context) -> {

            MinecraftServer.getSchedulerManager().submitTask(() -> {
                sender.sendMessage(Component.text("正在获取中...", NamedTextColor.GREEN));
                try {

                    boolean r18Mode = context.get(r18);

                    BufferedImage image = null;
                    Connection.Response response = Jsoup.connect(url+"?r18="+r18Mode)
                            .ignoreContentType(true)
                            .execute();
                    JSONObject json = JSONObject.parseObject(response.body());
                    if (json.getString("code").equals("200")) {
                        /*
                        JSONArray dataArray = json.getJSONArray("data");
                        JSONObject data = dataArray.getJSONObject(0);
                        JSONObject urlData = data.getJSONObject("urls");
                        String imgUrl = urlData.getString("original");

                         */

                        JSONObject data = json.getJSONObject("data");
                        JSONArray urls = data.getJSONArray("url");

                        String imgUrl = urls.getString(0);
                        URL img = new URL(imgUrl.replace("floral-disk-7293.nyancatda.workers.dev", "i.pixiv.re"));
                        URLConnection connection = img.openConnection();

                        InputStream inputStream = connection.getInputStream();
                        OutputStream outputStream = new FileOutputStream("/home/minestom/img/1.png");

                        int tmp = 0;
                        while ((tmp = inputStream.read()) != -1) {
                            outputStream.write(tmp);
                        }
                        outputStream.close();

                        image = ImageIO.read(new File("/home/minestom/img/1.png"));

                        Player player = (Player) sender;

                        LargeGraphics2DFramebuffer framebuffer = new LargeGraphics2DFramebuffer(image.getWidth(), image.getHeight());
                        int counter = 1;
                        framebuffer.getRenderer().drawRenderedImage(image, AffineTransform.getScaleInstance(1.0, 1.0));

                        final int maxX = 0;
                        final int maxY = framebuffer.height() / 128 + 40;
                        final int z = 9;

                        ((Player) sender).getInstance().getEntities().forEach(entity -> {
                            if (entity.getEntityType().equals(EntityType.ITEM_FRAME)) {
                                entity.remove();
                            }
                        });

                        for (int i = 0; i < (double) framebuffer.height() / 128 + 1; i++) {
                            for (int j = 0; j < (double) framebuffer.width() / 128 + 1; j++) {
                                Entity itemFrame = new Entity(EntityType.ITEM_FRAME);
                                ItemFrameMeta meta = (ItemFrameMeta) itemFrame.getEntityMeta();
                                itemFrame.setInstance(player.getInstance(), new Pos(maxX - i, maxY - j, z, 180, 0));
                                meta.setNotifyAboutChanges(false);
                                meta.setOrientation(ItemFrameMeta.Orientation.NORTH);
                                meta.setInvisible(true);
                                int finalCounter = counter;
                                meta.setItem(ItemStack.builder(Material.FILLED_MAP)
                                        .meta(MapMeta.class, builder -> builder.mapId(finalCounter))
                                        .build());
                                meta.setNotifyAboutChanges(true);

                                //((Player) sender).sendPacket(framebuffer.createSubView(128,128).preparePacket(counter));

                                GamePacket.sendPacket(((Player) sender), framebuffer.createSubView(128 * i, 128 * j).preparePacket(counter));

                                counter++;
                            }
                        }

                    }
                } catch (IOException e) {
                    sender.sendMessage(Component.text("获取失败，错误原因: "+e.getMessage(), NamedTextColor.RED));
                }
                return TaskSchedule.stop();
            });

        }, r18);
    }
}
