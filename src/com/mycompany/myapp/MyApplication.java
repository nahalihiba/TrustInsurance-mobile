package com.mycompany.myapp;


import Entity.User;
import GUI.EventList;
import GUI.EventListAdmin;
import Utils.ConnectedUser;
import com.codename1.components.ImageViewer;
import com.codename1.io.CharArrayReader;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.ui.Form;
import com.codename1.ui.Dialog;
import com.codename1.ui.Label;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.ui.Button;
import com.codename1.ui.Component;
import static com.codename1.ui.Component.RIGHT;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.FontImage;
import com.codename1.ui.Image;
import com.codename1.ui.TextField;
import com.codename1.ui.Toolbar;
import com.codename1.ui.URLImage;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.codename1.ui.validation.LengthConstraint;
import com.codename1.ui.validation.RegexConstraint;
import com.codename1.ui.validation.Validator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * This file was generated by <a href="https://www.codenameone.com/">Codename
 * One</a> for the purpose of building native mobile applications using Java.
 */
public class MyApplication {

    Form f;
    Container data;
    TextField email;
    TextField password;
    Button login;

    Resources theme;

    public void init(Object context) {
        theme = UIManager.initFirstTheme("/theme");
        Toolbar.setGlobalToolbar(true);
        f = new Form(BoxLayout.y());
        f.getToolbar().setEnabled(false);
        data = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        int deviceWidth = Display.getInstance().getDisplayWidth() / 2;
        System.out.println(deviceWidth);
        Image placeholder = Image.createImage(deviceWidth, deviceWidth); //square image set to 10% of screen width
        EncodedImage encImage = EncodedImage.createFromImage(placeholder, false);
        URLImage imgsv = URLImage.createToStorage(encImage,
                "http://localhost/img/logo.png", "http://localhost/img/logo.png", URLImage.RESIZE_SCALE_TO_FILL);
        ImageViewer imgvb = new ImageViewer(imgsv);
        email = new TextField();
        email.setHint("Email");
        password = new TextField();
        password.setHint("Password");
        password.setConstraint(TextField.PASSWORD);
        login = new Button("LOGIN");
        Label loginIcon = new Label("", "TextField");
        Label passwordIcon = new Label("", "TextField");
        loginIcon.getAllStyles().setMargin(RIGHT, 0);
        passwordIcon.getAllStyles().setMargin(RIGHT, 0);
        FontImage.setMaterialIcon(loginIcon, FontImage.MATERIAL_PERSON_OUTLINE, 3);
        FontImage.setMaterialIcon(passwordIcon, FontImage.MATERIAL_LOCK_OUTLINE, 3);
        email.setText("hiba11@gmail.com");
        password.setText("123");
        data.add(BorderLayout.center(email).
                add(BorderLayout.WEST, loginIcon));
        data.add(createLineSeparator());
        data.add(BorderLayout.center(password).
                add(BorderLayout.WEST, passwordIcon));
        data.add(createLineSeparator());
        Validator v = new Validator();
        v.addConstraint(email, RegexConstraint.validEmail()).addConstraint(password, new LengthConstraint(2, "too small"));
        v.addSubmitButtons(login);
        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if (v.isValid()) {
                    ConnectionRequest con = new ConnectionRequest();
                    String name = email.getText();
                    String pswd = password.getText();
                    con.setUrl("http://localhost/PIJFinal/web/app_dev.php/mobile/loginM?email=" + name + "&password=" + pswd);

                    con.addResponseListener(new ActionListener<NetworkEvent>() {
                        @Override
                        public void actionPerformed(NetworkEvent evt) {
                            try {
                                String json = new String(con.getResponseData());
                                JSONParser j = new JSONParser();

                                Map<String, Object> users = j.parseJSON(new CharArrayReader(json.toCharArray()));

                                if (users.get("id") == null) {
                                    Dialog.show("Erreur d'authentification", "Verifier votre Nom d'utilisateur ou mot de passe!!", "OK", "Annuler");
                                } else {

                                    User u = new User();
                                    float id = Float.parseFloat(users.get("id").toString());
                                    u.setId((int) id);
                                    u.setEmail(users.get("email").toString());
                                    String Roles = users.get("roles").toString();
                                    u.setRoles(Roles);
                                    ConnectedUser.setUser(u);
                                    System.out.println(u.getId());
                                    ConnectedUser.setUser(u);
                                   if (ConnectedUser.getUser().getRoles().indexOf("ROLE_CLIENT") > -1) {
                                        EventList h = new EventList();
                                        h.getF().show();
                                    } else {
                                        EventListAdmin h = new EventListAdmin();
                                        h.getF().showBack();
                                    }

                                }

                            } catch (IOException ex) {
                            }
                        }
                    });
                    NetworkManager.getInstance().addToQueue(con);
                }
            }
        });
        data.add(login);
        f.add(imgvb);
        f.add(data);
    }

    public void start() {
        if (f != null) {
            f.show();

        }
    }

    public void stop() {

    }

    public Component createLineSeparator() {
        Label separator = new Label("", "WhiteSeparator");
        separator.setShowEvenIfBlank(true);
        return separator;
    }

    public void destroy() {
    }

}
