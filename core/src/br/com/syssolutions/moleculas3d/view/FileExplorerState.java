package br.com.syssolutions.moleculas3d.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import java.io.File;

import br.com.syssolutions.moleculas3d.Utils.FontGenerator;
import br.com.syssolutions.moleculas3d.control.states.GameStateManager;
import br.com.syssolutions.moleculas3d.control.states.State;
import br.com.syssolutions.moleculas3d.model.Molecula;
import br.com.syssolutions.moleculas3d.model.Moleculas3D;
import br.com.syssolutions.moleculas3d.model.ReadMoleculaXML;

/**
 * Created by jefferson on 24/10/17.
 */

public class FileExplorerState extends State {



    private static String lastPath;

    private OrthographicCamera camOrtho;
    private FileHandle[] listFiles;


    private Texture background;
    private Skin skin;
    private TextureAtlas textureAtlas;

    private Texture folderT, fileT, molT, folderUP;

    private Table container;

    private Stage stage;
    private ScrollPane scrollpane;
    private LabelStyle labelStyle;


    public FileExplorerState(GameStateManager gsm, String path) {
        super(gsm);

        init();

        try{
            FileHandle fileHandle = Gdx.files.external(path);

            showFilesEx(fileHandle);

        }catch (Exception e){

            try{

                FileHandle fileHandle = Gdx.files.external(path);

                //showFilesAb(fileHandle);


            }catch (Exception ex){

            }

        }

    }


    private void init(){
        camOrtho = new OrthographicCamera();
        camOrtho.setToOrtho(false, Moleculas3D.WIDTH, Moleculas3D.HEIGHT);
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        skin = new Skin();


        folderT = new Texture(Gdx.files.internal("ui-imagens/folder.png"));
        fileT = new Texture(Gdx.files.internal("ui-imagens/file.png"));
        molT = new Texture(Gdx.files.internal("ui-imagens/mol.png"));
        folderUP = new Texture(Gdx.files.internal("ui-imagens/folderUP.png"));


        background = new Texture("ui-imagens/background.jpg");
        labelStyle = new Label.LabelStyle();
        FontGenerator fontTitulo = new FontGenerator(40, "VeraBd.ttf", null);
        labelStyle.font = fontTitulo.getFont();


    }

    public FileExplorerState(GameStateManager gsm) {
        super(gsm);

        init();


        showStorageAvailable();

    }


    private void showStorageAvailable() {

        container = new Table();
        container.setWidth(Gdx.graphics.getWidth());
        container.setHeight(Gdx.graphics.getHeight());

        Table innerContainer = new Table();
        innerContainer.align(Align.topLeft);


        if (Gdx.files.external("").isDirectory()) {


            //System.out.println("Patch do diretório External: " + Gdx.files.external("").path());


            Table table = new Table(skin);
            //table.debugTable();
            final FileHandle file = Gdx.files.external("");

            if (file.isDirectory()) {


                table.add(new Image(folderT)).expandY().fillY().space(10f).left();
                table.add(new Label("Interno", labelStyle)).expandY().fillY().width(container.getWidth() - folderT.getWidth()).left();

                table.align(Align.left);


                table.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {

                        showFilesEx(Gdx.files.external(file.path()));
                    }
                });

                innerContainer.add(table);//.expand().fill();

                innerContainer.row();

                //scrollpane = new ScrollPane(innerContainer);
                //   scrollpane.setScrollingDisabled(true, false);
                // container.add(scrollpane).fill().expand();


//                stage = new Stage();
//                stage.addActor(container);
//                Gdx.input.setInputProcessor(stage);

            }
        }

        try {
            for (File file : Moleculas3D.iEnvironmentAndroid.getRemovabeStorages()) {

                Table table = new Table(skin);
                final FileHandle f = Gdx.files.absolute(file.getAbsolutePath());


                // System.out.println("Path do Cartão: " + file.getAbsolutePath());


                if (f.isDirectory()) {


                    table.add(new Image(folderT)).expandY().fillY().space(10f).left();
                    table.add(new Label("Externo", labelStyle)).expandY().fillY().width(container.getWidth() - folderT.getWidth()).left();

                    table.align(Align.left);


                    table.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            //System.out.println("Path() Atual: " + f.path());
                            //atualPatch = f.parent().toString();
                            showFilesAb(Gdx.files.absolute(f.path()), f.path().toString());
                        }
                    });

                    innerContainer.add(table);//.expand().fill();

                    innerContainer.row();

                }


            }
        } catch (NullPointerException ex) {
            System.out.println(ex);
        }

        scrollpane = new ScrollPane(innerContainer);
        scrollpane.setScrollingDisabled(true, false);
        container.add(scrollpane).fill().expand();

        stage = new Stage();
        stage.addActor(container);
        Gdx.input.setInputProcessor(stage);


    }


    public void showFilesEx(final FileHandle fileHandle) {


        container = new Table();
        container.setWidth(Gdx.graphics.getWidth());
        container.setHeight(Gdx.graphics.getHeight());
        // container.setFillParent(false);

        Table innerContainer = new Table();
        innerContainer.align(Align.topLeft);


        if (fileHandle.isDirectory()) {

            Table backTable = new Table(skin);
            backTable.add(new Image(folderUP)).expandY().fillY().space(10f).left();
            backTable.add(new Label("..", labelStyle)).expandY().fillY().width(container.getWidth() - folderT.getWidth()).left();
            backTable.align(Align.left);


            backTable.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {

                    if (fileHandle.path().toString() == "") {
                        showStorageAvailable();
                    } else
                        showFilesEx(Gdx.files.external(fileHandle.parent().toString()));


                }
            });


            innerContainer.add(backTable);
            innerContainer.row();


            for (final FileHandle file : fileHandle.list()) {
                Table table = new Table(skin);
                //table.debugTable();
                final FileHandle fl = file;

                if (file.isDirectory()) {


                    table.add(new Image(folderT)).expandY().fillY().space(10f).left();
                    table.add(new Label(file.name(), labelStyle)).expandY().fillY().width(container.getWidth() - folderT.getWidth()).left();

                    table.align(Align.left);


                    table.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            showFilesEx(Gdx.files.external(fl.path()));
                        }
                    });

                    innerContainer.add(table);//.expand().fill();

                    innerContainer.row();

                } else {
                    if (!(file.extension().equals("cml") ||
                            file.extension().equals("xyz") ||
                            file.extension().equals("mol") ||
                            file.extension().equals("mol2"))) {
                        //innerContainer.add(new Image(fileT)).expandY().fillY();

                        table.add(new Image(fileT)).expandY().fillY().space(10f).left();
                        table.add(new Label(file.name(), labelStyle)).expandY().fillY().width(container.getWidth() - fileT.getWidth()).left();

                    } else {
                        //innerContainer.add(new Image(molT)).expandY().fillY();
                        table.add(new Image(molT)).expandY().fillY().space(10f).left();
                        table.add(new Label(file.name(), labelStyle)).expandY().fillY().width(container.getWidth() - molT.getWidth()).left();


                        table.addListener(new ClickListener() {
                            @Override
                            public void clicked(InputEvent event, float x, float y) {


                                try {
                                    Molecula mol = ReadMoleculaXML.read(fl, false);

                                    lastPath=fl.path();
                                    Visualizador3DState.setMolecula(mol);
                                    gsm.set(new Visualizador3DState(gsm));
                                    dispose();
                                } catch (Exception e) {
                                    System.out.println("Falha ao carregar molécula" + e);
                                }


                            }
                        });


                    }

                    table.align(Align.left);
                    innerContainer.add(table);


//                    innerContainer.add(new Label("", labelStyle)).width(10f).expandY().fillY();// a spacer
//                    innerContainer.add(new Label(file.name(), labelStyle)).expandY().fillY().left();

                    innerContainer.row();

                }

            }
        } else {
            System.out.println("Não é diretório");
        }


        scrollpane = new ScrollPane(innerContainer);
        scrollpane.setScrollingDisabled(true, false);
        container.add(scrollpane).fill().expand();

        stage = new Stage();
        stage.addActor(container);
        Gdx.input.setInputProcessor(stage);


    }


    public void showFilesAb(final FileHandle fileHandle, String raiz) {

        final String root = raiz;


        container = new Table();
        container.setWidth(Gdx.graphics.getWidth());
        container.setHeight(Gdx.graphics.getHeight());
        // container.setFillParent(false);

        Table innerContainer = new Table();
        innerContainer.align(Align.topLeft);


        if (fileHandle.isDirectory()) {

            final Table backTable = new Table(skin);
            backTable.add(new Image(folderUP)).expandY().fillY().space(10f).left();


            backTable.add(new Label("..", labelStyle)).expandY().fillY().width(container.getWidth() - folderT.getWidth()).left();
            backTable.align(Align.left);


            backTable.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {

                    if ((fileHandle.path().toString().equals(root))/*||fileHandle.parent().path().toString()==root*/) {

                        showStorageAvailable();
                    } else {
                        showFilesAb(Gdx.files.absolute(fileHandle.parent().toString()), root);
                    }


                }
            });


            innerContainer.add(backTable);
            innerContainer.row();


            for (FileHandle file : fileHandle.list()) {
                Table table = new Table(skin);
                //table.debugTable();
                final FileHandle fl = file;

                if (file.isDirectory()) {


                    table.add(new Image(folderT)).expandY().fillY().space(10f).left();
                    table.add(new Label(file.name(), labelStyle)).expandY().fillY().width(container.getWidth() - folderT.getWidth()).left();

                    table.align(Align.left);


                    table.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {

                            showFilesAb(Gdx.files.absolute(fl.path()), root);
                        }
                    });

                    innerContainer.add(table);//.expand().fill();

                    innerContainer.row();

                } else {

                    if (!(file.extension().equals("cml") ||
                            file.extension().equals("xyz") ||
                            file.extension().equals("mol") ||
                            file.extension().equals("mol2"))) {
                        //innerContainer.add(new Image(fileT)).expandY().fillY();

                        table.add(new Image(fileT)).expandY().fillY().space(10f).left();
                        table.add(new Label(file.name(), labelStyle)).expandY().fillY().width(container.getWidth() - fileT.getWidth()).left();

                    } else {
                        //innerContainer.add(new Image(molT)).expandY().fillY();
                        table.add(new Image(molT)).expandY().fillY().space(10f).left();
                        table.add(new Label(file.name(), labelStyle)).expandY().fillY().width(container.getWidth() - molT.getWidth()).left();


                        table.addListener(new ClickListener() {
                            @Override
                            public void clicked(InputEvent event, float x, float y) {


                                try {
                                    Molecula mol = ReadMoleculaXML.read(fl, true);
                                    lastPath=fl.path();

                                    Visualizador3DState.setMolecula(mol);
                                    gsm.set(new Visualizador3DState(gsm));
                                    dispose();
                                } catch (Exception e) {
                                    System.out.println("Falha ao carregar molécula" + e);
                                }


                            }
                        });

                    }

                    table.align(Align.left);
                    innerContainer.add(table);


//                    innerContainer.add(new Label("", labelStyle)).width(10f).expandY().fillY();// a spacer
//                    innerContainer.add(new Label(file.name(), labelStyle)).expandY().fillY().left();

                    innerContainer.row();

                }

            }
        } else {
            System.out.println("Não é diretório");
        }


        scrollpane = new ScrollPane(innerContainer);
        scrollpane.setScrollingDisabled(true, false);
        container.add(scrollpane).fill().expand();

        stage = new Stage();
        stage.addActor(container);
        Gdx.input.setInputProcessor(stage);


    }


    @Override
    protected void handleInput() {
//        if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
//
//            showFilesEx(Gdx.files.external(dirHandle.parent().toString()));
//
//        }


    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {

        sb.setProjectionMatrix(camOrtho.combined);
        sb.begin();
        //sb.draw(background, 0, 0, camOrtho.viewportWidth, camOrtho.viewportHeight);
        sb.end();


        stage.act();
        stage.draw();


    }

    @Override
    public void dispose() {

    }
}
