/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controller;

import hr.algebra.dao.RepositoryFactory;
import hr.algebra.dao.RepositoryShoesFactory;
import hr.algebra.utilities.FileUtils;
import hr.algebra.utilities.ImageUtils;
import hr.algebra.utilities.ValidationUtils;
import hr.algebra.viewmodel.PersonViewModel;
import hr.algebra.viewmodel.ShoesViewModel;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.beans.property.IntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.converter.IntegerStringConverter;

/**
 * FXML Controller class
 *
 * @author Next Design
 */
public class PeopleController implements Initializable {
    
    private Map<TextField, Label> validationMap;
    private Map<TextField, Label> validationShoesMap;

    private final ObservableList<PersonViewModel> people = FXCollections.observableArrayList();
    private final ObservableList<ShoesViewModel> shoes = FXCollections.observableArrayList();

    private PersonViewModel selectedPersonViewModel;
    private ShoesViewModel selectedShoesViewModel;

    private Tab previousTab;

    @FXML
    private TabPane tpContent;
    @FXML
    private Tab tabList;
    @FXML
    private TableView<PersonViewModel> tvPeople;
    @FXML
    private TableColumn<PersonViewModel, String> tcFirstName;
    @FXML
    private TableColumn<PersonViewModel, String> tcLastName;
    @FXML
    private TableColumn<PersonViewModel, Integer> tcAge;
    @FXML
    private TableColumn<PersonViewModel, String> tcEmail;

    
    //people tab
    @FXML
    private Tab tabEdit;
    @FXML
    private ImageView ivPerson;
    @FXML
    private TextField tfFirstName;
    @FXML
    private TextField tfLastName;
    @FXML
    private TextField tfAge;
    @FXML
    private TextField tfEmail;
    @FXML
    private Label lbFirstNameError;
    @FXML
    private Label lbAgeError;
    @FXML
    private Label lbLastNameError;
    @FXML
    private Label lbEmailError;
    @FXML
    private Label lbIconError;
    
    
    //shoes tab
    @FXML
    private TableView<ShoesViewModel> tvShoes;
    @FXML
    private TableColumn<ShoesViewModel, String> tcBrand;
    @FXML
    private TableColumn<ShoesViewModel, Integer> tcSize;
    @FXML
    private ImageView ivShoes;
    @FXML
    private TextField tfBrand;
    @FXML
    private TextField tfSize;
    @FXML
    private Label lbBrandError;
    @FXML
    private Label lbSizeError;
    @FXML
    private Label lbShoesIconError;
    @FXML
    private Tab tabShoesList;
    @FXML
    private Tab tabShoesEdit;
    
    
    @FXML
    private AnchorPane rootPane;
    @FXML
    private Button btnGoToShoes;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initValidation();
        initShoesValidation();
        initPeople();
        initTable();
        addIntegerMask(tfAge, tfSize); 
        setupListeners();
    }    

    @FXML
    private void delete(ActionEvent event) {
        if (tvPeople.getSelectionModel().getSelectedItem() != null) {
            people.remove(tvPeople.getSelectionModel().getSelectedItem());
        }
    }

    @FXML
    private void edit(ActionEvent event) {
        if (tvPeople.getSelectionModel().getSelectedItem() != null) {
            bindPerson(tvPeople.getSelectionModel().getSelectedItem());
            tpContent.getSelectionModel().select(tabEdit);
            previousTab = tabEdit;
        }
    }   

    @FXML
    private void commit(ActionEvent event) {
        if (formValid()) {
            selectedPersonViewModel.getPerson().setFirstName(tfFirstName.getText().trim());
            selectedPersonViewModel.getPerson().setLastName(tfLastName.getText().trim());
            selectedPersonViewModel.getPerson().setAge(Integer.valueOf(tfAge.getText().trim()));
            selectedPersonViewModel.getPerson().setEmail(tfEmail.getText().trim());
            if (selectedPersonViewModel.getPerson().getIDPerson() == 0) {
                people.add(selectedPersonViewModel);
            } else {
                try {
                    // we cannot listen to the upates
                    RepositoryFactory.getRepository().updatePerson(selectedPersonViewModel.getPerson());
                    tvPeople.refresh();
                } catch (Exception ex) {
                    Logger.getLogger(PeopleController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            selectedPersonViewModel = null;
            tpContent.getSelectionModel().select(tabList);
            resetForm();
        }
    }

    @FXML
    private void uploadImage(ActionEvent event) {
        File file = FileUtils.uploadFileDialog(tfAge.getScene().getWindow(), "jpg", "jpeg", "png");
        if (file != null) {
            Image image = new Image(file.toURI().toString());
            try {
                String ext = file.getName().substring(file.getName().lastIndexOf(".") + 1);
                selectedPersonViewModel.getPerson().setPicture(ImageUtils.imageToByteArray(image, ext));
                ivPerson.setImage(image);
            } catch (IOException ex) {
                Logger.getLogger(PeopleController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void initValidation() {
        validationMap = Stream.of(
                new AbstractMap.SimpleImmutableEntry<>(tfFirstName, lbFirstNameError),
                new AbstractMap.SimpleImmutableEntry<>(tfLastName, lbLastNameError),
                new AbstractMap.SimpleImmutableEntry<>(tfAge, lbAgeError),
                new AbstractMap.SimpleImmutableEntry<>(tfEmail, lbEmailError))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
    
    private void initPeople() {
        try {
            RepositoryFactory.getRepository().getPeople().forEach(person -> people.add(new PersonViewModel(person)));
        } catch (Exception ex) {
            Logger.getLogger(PeopleController.class.getName()).log(Level.SEVERE, null, ex);
            new Alert(Alert.AlertType.ERROR, "Unable to load the form. Exiting...").show();
        }
    }

    private void initTable() {
        tcFirstName.setCellValueFactory(person -> person.getValue().getFirstNameProperty());
        tcLastName.setCellValueFactory(person -> person.getValue().getLastNameProperty());
        tcAge.setCellValueFactory(person -> person.getValue().getAgeProperty().asObject());
        tcEmail.setCellValueFactory(person -> person.getValue().getEmailProperty());
        tvPeople.setItems(people);
    }

    private void addIntegerMask(TextField tfAge, TextField tfSize) {
        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String input = change.getText();
            if (input.matches("\\d*")) {
                return change;
            }
            return null;
        };
        // first we must convert integer to string, and then we apply filter
        tfAge.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0, integerFilter));
        tfSize.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0, integerFilter));
    }

    private void setupListeners() {
        tpContent.setOnMouseClicked(event -> {
            if (tpContent.getSelectionModel().getSelectedItem().equals(tabEdit) && !tabEdit.equals(previousTab)) {
                bindPerson(null);
            }
            System.out.println(previousTab);
            previousTab = tpContent.getSelectionModel().getSelectedItem();

        });
        people.addListener((ListChangeListener.Change<? extends PersonViewModel> change) -> {
            if (change.next()) {
                if (change.wasRemoved()) {
                    change.getRemoved().forEach(pvm -> {
                        try {
                            RepositoryFactory.getRepository().deletePerson(pvm.getPerson());
                        } catch (Exception ex) {
                            Logger.getLogger(PeopleController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                } else if (change.wasAdded()) {
                    change.getAddedSubList().forEach(pvm -> {
                        try {
                            int idPerson = RepositoryFactory.getRepository().addPerson(pvm.getPerson());
                            pvm.getPerson().setIDPerson(idPerson);
                        } catch (Exception ex) {
                            Logger.getLogger(PeopleController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                }
            }
        });
        
        
        shoes.addListener((ListChangeListener.Change<? extends ShoesViewModel> change) -> {
            if (change.next()) {
                if (change.wasRemoved()) {
                    change.getRemoved().forEach(pvm -> {
                        try {
                            RepositoryShoesFactory.getShoesRepository().deleteShoes(pvm.getShoes());
                        } catch (Exception ex) {
                            Logger.getLogger(PeopleController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                } else if (change.wasAdded()) {
                    change.getAddedSubList().forEach(pvm -> {
                        try {
                            int idShoes = RepositoryShoesFactory.getShoesRepository().addShoes(pvm.getShoes());
                            pvm.getShoes().setIDShoes(idShoes);
                        } catch (Exception ex) {
                            Logger.getLogger(PeopleController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                }
            }
        });
    }

    private void bindPerson(PersonViewModel viewModel) {
        resetForm();
        selectedPersonViewModel = viewModel != null ? viewModel : new PersonViewModel(null);
        tfFirstName.setText(selectedPersonViewModel.getFirstNameProperty().get());
        tfLastName.setText(selectedPersonViewModel.getLastNameProperty().get());
        tfAge.setText(String.valueOf(selectedPersonViewModel.getAgeProperty().get()));
        tfEmail.setText(selectedPersonViewModel.getEmailProperty().get());
        ivPerson.setImage(selectedPersonViewModel.getPictureProperty().get() != null ? new Image(new ByteArrayInputStream(selectedPersonViewModel.getPictureProperty().get())) : new Image(new File("src/assets/no_image.png").toURI().toString()));
    }
    
    
    
    
    
    
    

    //PROJEKTNI DIO ZADATKA
    
    private void resetShoesForm() {
        validationShoesMap.values().forEach(lb -> lb.setVisible(false));
        lbIconError.setVisible(false);
    }

    private void resetForm() {
        validationMap.values().forEach(lb -> lb.setVisible(false));
        lbIconError.setVisible(false);
    }

    private boolean formValid() {
        // discouraged but ok!
        final AtomicBoolean ok = new AtomicBoolean(true);
        validationMap.keySet().forEach(tf -> {
            if (tf.getText().trim().isEmpty() || tf.getId().contains("Email") && !ValidationUtils.isValidEmail(tf.getText().trim())) {
                validationMap.get(tf).setVisible(true);
                ok.set(false);
            } else {
                validationMap.get(tf).setVisible(false);
            }
        });

        if (selectedPersonViewModel.getPictureProperty().get() == null) {
            lbIconError.setVisible(true);
            ok.set(false);
        } else {
            lbIconError.setVisible(false);
        }
        return ok.get();
    }
    
    private void initShoesValidation() {
    validationShoesMap = Stream.of(
            new AbstractMap.SimpleImmutableEntry<>(tfBrand, lbBrandError),
            new AbstractMap.SimpleImmutableEntry<>(tfSize, lbSizeError))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
    
    
    private void bindShoes(ShoesViewModel selectedItem) {
        resetShoesForm();
        selectedShoesViewModel = selectedItem != null ? selectedItem : new ShoesViewModel(null);
        tfBrand.setText(selectedShoesViewModel.getBrandProperty().get());
        tfSize.setText(String.valueOf(selectedShoesViewModel.getSizeProperty().get()));
        ivShoes.setImage(selectedShoesViewModel.getShoesPictureProperty().get() != null ? new Image(new ByteArrayInputStream(selectedShoesViewModel.getShoesPictureProperty().get())) : new Image(new File("src/assets/no_image.png").toURI().toString()));
    }

    @FXML
    private void deleteShoes(ActionEvent event) {
        if (tvShoes.getSelectionModel().getSelectedItem() != null) {
            shoes.remove(tvShoes.getSelectionModel().getSelectedItem());    
        }
    }

    @FXML
    private void editShoes(ActionEvent event) {
        if (tvShoes.getSelectionModel().getSelectedItem() != null) {
            bindShoes(tvShoes.getSelectionModel().getSelectedItem());
            tpContent.getSelectionModel().select(tabShoesEdit);
            previousTab = tabShoesEdit;
        }
    }

    @FXML
    private void commitShoes(ActionEvent event) {
        if (formShoesValid()) {
            selectedShoesViewModel.getShoes().setBrand(tfBrand.getText().trim());
            selectedShoesViewModel.getShoes().setSize(Integer.valueOf(tfSize.getText().trim()));
            
            if (selectedShoesViewModel.getShoes().getIDShoes() == 0) {
                shoes.add(selectedShoesViewModel);
            } else {
                try {
                    // we cannot listen to the upates
                    RepositoryShoesFactory.getShoesRepository().updateShoes(selectedShoesViewModel.getShoes());
                    tvShoes.refresh();
                } catch (Exception ex) {
                    Logger.getLogger(PeopleController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            selectedShoesViewModel = null;
            tpContent.getSelectionModel().select(tabShoesList);
            resetForm();
        }
        
    }

    @FXML
    private void uploadShoesImage(ActionEvent event) {
        File file = FileUtils.uploadFileDialog(tfSize.getScene().getWindow(), "jpg", "jpeg", "png");
        if (file != null) {
            Image image = new Image(file.toURI().toString());
            try {
                String ext = file.getName().substring(file.getName().lastIndexOf(".") + 1);
                selectedShoesViewModel.getShoes().setShoesPicture(ImageUtils.imageToByteArray(image, ext));
                ivShoes.setImage(image);
            } catch (IOException ex) {
                Logger.getLogger(PeopleController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
  
    
    @FXML
    private void gotoShoes(MouseEvent event) throws IOException, Exception {
        if (tvPeople.getSelectionModel().getSelectedItem() != null) {
            PersonViewModel p = tvPeople.getSelectionModel().getSelectedItem();
            initShoes(p.getIdPersonProperty().getValue());
            
            bindShoes(tvShoes.getSelectionModel().getSelectedItem());
            tpContent.getSelectionModel().select(tabShoesList);
            initShoesTable(tvShoes.getSelectionModel().getSelectedItem());
            previousTab = tabList;
        }    
    }
    
    private void initShoesTable(ShoesViewModel selectedItem) {
        tcBrand.setCellValueFactory(s -> s.getValue().getBrandProperty());
        tcSize.setCellValueFactory(s -> s.getValue().getSizeProperty().asObject());
        tvShoes.setItems(shoes);
    }

    private void initShoes(Integer idPerson) {
        try {
            RepositoryShoesFactory.getShoesRepository().getAllShoes(idPerson).forEach(s -> shoes.add(new ShoesViewModel(s)));
        } catch (Exception ex) {
            Logger.getLogger(PeopleController.class.getName()).log(Level.SEVERE, null, ex);
            new Alert(Alert.AlertType.ERROR, "Unable to load the form. Exiting...").show();
        }
    }

    private boolean formShoesValid() {
        // discouraged but ok!
        final AtomicBoolean ok = new AtomicBoolean(true);
        validationMap.keySet().forEach(tf -> {
            if (tf.getText().trim().isEmpty() || tf.getId().contains("Email") && !ValidationUtils.isValidEmail(tf.getText().trim())) {
                validationMap.get(tf).setVisible(true);
                ok.set(false);
            } else {
                validationMap.get(tf).setVisible(false);
            }
        });

        if (selectedPersonViewModel.getPictureProperty().get() == null) {
            lbIconError.setVisible(true);
            ok.set(false);
        } else {
            lbIconError.setVisible(false);
        }
        return ok.get();
    }

    
}
