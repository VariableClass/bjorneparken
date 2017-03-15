/**
 @fileoverview
 Provides methods to be used across the Bjørneparkappen Administrative Console
 */

/** Main */

// Base REST API request URL
const BASE_URL = "https://api-dot-bjorneparkappen.appspot.com/_ah/api/bjorneparkappen_api/v1.0/";

// API key
const API_KEY = "AIzaSyCuD2dk_XFcn512V5JxAZbFlAK9dgNlQ9c";

// Language Code
const LANGUAGE_CODE = "en";

// Namespaces for Bjørneparken and the Administrative Console
var bjørneparkappen = bjørneparkappen || {};
bjørneparkappen.adminconsole = bjørneparkappen.adminconsole || {};
bjørneparkappen.adminconsole.api = bjørneparkappen.adminconsole.api || {};
bjørneparkappen.adminconsole.navigation = bjørneparkappen.adminconsole.navigation || {};
bjørneparkappen.adminconsole.species = bjørneparkappen.adminconsole.species || {};
bjørneparkappen.adminconsole.species.list = bjørneparkappen.adminconsole.species.list || {};
bjørneparkappen.adminconsole.species.detail = bjørneparkappen.adminconsole.species.detail || {};
bjørneparkappen.adminconsole.areas = bjørneparkappen.adminconsole.areas || {};
bjørneparkappen.adminconsole.areas.list = bjørneparkappen.adminconsole.areas.list || {};
bjørneparkappen.adminconsole.areas.detail = bjørneparkappen.adminconsole.areas.detail || {};
bjørneparkappen.adminconsole.animals = bjørneparkappen.adminconsole.animals || {};
bjørneparkappen.adminconsole.animals.list = bjørneparkappen.adminconsole.animals.list || {};
bjørneparkappen.adminconsole.animals.detail = bjørneparkappen.adminconsole.animals.detail || {};
bjørneparkappen.adminconsole.events = bjørneparkappen.adminconsole.events || {};
bjørneparkappen.adminconsole.events.list = bjørneparkappen.adminconsole.events.list || {};
bjørneparkappen.adminconsole.events.detail = bjørneparkappen.adminconsole.events.detail || {};
bjørneparkappen.adminconsole.keepers = bjørneparkappen.adminconsole.keepers || {};
bjørneparkappen.adminconsole.keepers.list = bjørneparkappen.adminconsole.keepers.list || {};
bjørneparkappen.adminconsole.keepers.detail = bjørneparkappen.adminconsole.keepers.detail || {};

// To be used in setting the currently selected nav item
const ACTIVE = "active";

// To be used in setting the currently displayed page
const DISPLAYED = "block";
const HIDDEN = "none";

// Element to display whenever a operation requires the user to wait
var loadingText = document.getElementById('loading-text');

// Tracks number of API calls awaiting a response
var activeApiCalls = 0;

// Puts the application UI into a wait state
bjørneparkappen.adminconsole.startWait = function(){

    // Increase the number of active API calls
    activeApiCalls += 1;

    loadingText.style.display = DISPLAYED;
};

// Terminates the application UI wait state
bjørneparkappen.adminconsole.endWait = function(){

    activeApiCalls -= 1;

    if (activeApiCalls <= 0){

        loadingText.style.display = HIDDEN;
    }
};

// Initialises the application
bjørneparkappen.adminconsole.init = function() {

    // Retrieve all data asynchronously
    bjørneparkappen.adminconsole.api.getSpecies();
    bjørneparkappen.adminconsole.api.getAreas();
    bjørneparkappen.adminconsole.api.getAnimals();
    bjørneparkappen.adminconsole.api.getEvents();
    bjørneparkappen.adminconsole.api.getKeepers();
};


/** Navigation */
// Deselects all nav items
bjørneparkappen.adminconsole.navigation.deselectAllNavItems = function(){
    var activeNavItems = document.getElementsByClassName(ACTIVE);

    for (var i = 0; i < activeNavItems.length; i++) {
       activeNavItems[i].classList.remove(ACTIVE);
    }
};

// Selects a single nav item
bjørneparkappen.adminconsole.navigation.selectNavItem = function(navItem){

    // Deselct all nav items
    bjørneparkappen.adminconsole.navigation.deselectAllNavItems();

    // Select nav item
    navItem.className = ACTIVE;
};

// Hides all pages
bjørneparkappen.adminconsole.navigation.hideAllPages = function(){

    speciesDetailPage.style.display = HIDDEN;
    listSpeciesPage.style.display = HIDDEN;
    areaDetailPage.style.display = HIDDEN;
    listAreasPage.style.display = HIDDEN;
    animalDetailPage.style.display = HIDDEN;
    listAnimalsPage.style.display = HIDDEN;
    eventDetailPage.style.display = HIDDEN;
    listEventsPage.style.display = HIDDEN;
    keeperDetailPage.style.display = HIDDEN;
    listKeepersPage.style.display = HIDDEN;
};

// Displays a single page
bjørneparkappen.adminconsole.navigation.displayPage = function(page){

    // Hide all pages
    bjørneparkappen.adminconsole.navigation.hideAllPages();

    // Display selected page
    page.style.display = DISPLAYED;
};



/** Common Functions */
// Table search filter function
bjørneparkappen.adminconsole.lookup = function(search, table){

    var filter = search.value.toUpperCase();
    var tr = table.getElementsByTagName("tr");

    // For each table row
    for (i = 1; i < tr.length; i++) {

        td = tr[i].getElementsByTagName("td");

        var fruitlessColumns = 0;

        // For each column
        for (j = 0; j < td.length; j++) {

            if (td[j]) {

                // If text matches filter
                if (td[j].innerHTML.toUpperCase().indexOf(filter) > -1) {

                    fruitlessColumns = 0;
                    break;

                } else {

                    fruitlessColumns++;
                }
            }
        }

        // If no matching text found
        if (fruitlessColumns == td.length){

            // Hide row
            tr[i].style.display = HIDDEN;

        } else {

            // Show row
            tr[i].style.display = "";
        }
    }
}



/** API Calls */
bjørneparkappen.adminconsole.api.getSpecies = function(){

    // Put UI into wait state
    bjørneparkappen.adminconsole.startWait();

    // Create new request
    var xhr = new XMLHttpRequest();

    // Open new GET request
    xhr.open('GET', BASE_URL + "species/all?language_code=" + LANGUAGE_CODE + "&key=" + API_KEY);

    // GET request state change callback event
    xhr.onreadystatechange = function() {

        // If request has completed
        if (xhr.readyState == XMLHttpRequest.DONE){

            // If request status is 200
            if (xhr.status == 200){

                // Parse response JSON
                resp = JSON.parse(xhr.responseText);
                species = resp.species || [];
            }

            // Terminate UI wait state
            bjørneparkappen.adminconsole.endWait();
        }
    };

    // Send request
    xhr.send();
};
bjørneparkappen.adminconsole.api.getAreas = function(){
    // Put UI into wait state
    bjørneparkappen.adminconsole.startWait();

    // Create new request
    var xhr = new XMLHttpRequest();

    // Open new GET request
    xhr.open('GET', BASE_URL + "areas/all?language_code=" + LANGUAGE_CODE + "&key=" + API_KEY);

    // GET request state change callback event
    xhr.onreadystatechange = function() {

        // If request has completed
        if (xhr.readyState == XMLHttpRequest.DONE){

            // If request status is 200
            if (xhr.status == 200){

                // Parse response JSON
                resp = JSON.parse(xhr.responseText);
                amenities = resp.amenities || [];
                enclosures = resp.enclosures || [];
            }

            // Terminate UI wait state
            bjørneparkappen.adminconsole.endWait();
        }
    };

    // Send request
    xhr.send();
};
bjørneparkappen.adminconsole.api.getEvents = function(){
    // Put UI into wait state
    bjørneparkappen.adminconsole.startWait();

    // Create new request
    var xhr = new XMLHttpRequest();

    // Open new GET request
    xhr.open('GET', BASE_URL + "events/all?language_code=" + LANGUAGE_CODE + "&key=" + API_KEY);

    // GET request state change callback event
    xhr.onreadystatechange = function() {

        // If request has completed
        if (xhr.readyState == XMLHttpRequest.DONE){

            // If request status is 200
            if (xhr.status == 200){

                // Parse response JSON
                resp = JSON.parse(xhr.responseText);
                events = resp.events || [];
                feedings = resp.feedings || [];
            }

            // Terminate UI wait state
            bjørneparkappen.adminconsole.endWait();
        }
    };

    // Send request
    xhr.send();
};
bjørneparkappen.adminconsole.api.getAnimals = function(){

    // Put UI into wait state
    bjørneparkappen.adminconsole.startWait();

    // Create new request
    var xhr = new XMLHttpRequest();

    // Open new GET request
    xhr.open('GET', BASE_URL + "animals/all?language_code=" + LANGUAGE_CODE + "&key=" + API_KEY);

    // GET request state change callback event
    xhr.onreadystatechange = function() {

        // If request has completed
        if (xhr.readyState == XMLHttpRequest.DONE){

            // If request status is 200
            if (xhr.status == 200){

                // Parse response JSON
                resp = JSON.parse(xhr.responseText);
                animals = resp.animals || [];
            }

            // Terminate UI wait state
            bjørneparkappen.adminconsole.endWait();
        }
    };

    // Send request
    xhr.send();
};
bjørneparkappen.adminconsole.api.getKeepers = function(){

    // Put UI into wait state
    bjørneparkappen.adminconsole.startWait();

    // Create new request
    var xhr = new XMLHttpRequest();

    // Open new GET request
    xhr.open('GET', BASE_URL + "keepers/all?language_code=" + LANGUAGE_CODE + "&key=" + API_KEY);

    // GET request state change callback event
    xhr.onreadystatechange = function() {

        // If request has completed
        if (xhr.readyState == XMLHttpRequest.DONE){

            // If request status is 200
            if (xhr.status == 200){

                // Parse response JSON
                resp = JSON.parse(xhr.responseText);
                keepers = resp.keepers || [];
            }

            // Terminate UI wait state
            bjørneparkappen.adminconsole.endWait();
        }
    };

    // Send request
    xhr.send();
};



/** Species */
var species = [];

// Navigation Item
var speciesNav = document.getElementById('species');
speciesNav.onclick = function(){

    // Select Species nav item
    bjørneparkappen.adminconsole.navigation.selectNavItem(speciesNav);

    // Load List Species page
    bjørneparkappen.adminconsole.species.list.loadPage();
}


// List Species Page and elements
var listSpeciesPage = document.getElementById('list_species_page');
var createSpeciesButton = document.getElementById('list_species_create');
createSpeciesButton.onclick = function(){

    // Load the Create Species page
    bjørneparkappen.adminconsole.species.detail.loadCreatePage();

    // Display Create Species page
    bjørneparkappen.adminconsole.navigation.displayPage(speciesDetailPage);
}

// Clears the List Species page
bjørneparkappen.adminconsole.species.list.clearPageData = function(){

    // Clear search bar
    document.getElementById('list_species_search').value = "";

    // Retrieve table to empty
    var table = document.getElementById('list_species_table');

    // Empty table
    while(table.rows.length > 1) {

        table.deleteRow(1);
    }
};

// Loads the List Species page
bjørneparkappen.adminconsole.species.list.loadPage = function(){

    // Clear page data
    bjørneparkappen.adminconsole.species.list.clearPageData();

    // If no species were found
    if (species.length == 0){

        // Retrieve the list of species
        bjørneparkappen.adminconsole.api.getSpecies();
    }

    // Iterate the species list and add each to the table
    for (var i = 0; i < species.length; i++) {

        bjørneparkappen.adminconsole.species.addToTable(species[i]);
    }

    // Display the page
    bjørneparkappen.adminconsole.navigation.displayPage(listSpeciesPage);
};

// Adds a row to the Species table
bjørneparkappen.adminconsole.species.addToTable = function(species){

    var table = document.getElementById("list_species_table");

    // Create new table row
    var row = table.insertRow();

    // Create cells to insert into the table
    var commonNameCell = row.insertCell(0);
    var latinCell = row.insertCell(1);
    var deleteCell = row.insertCell(2);

    // Provide values to the new row
    commonNameCell.innerHTML = species.common_name;
    latinCell.innerHTML = species.latin;

    // Create link to delete item
    var deleteLink = document.createElement('a');
    deleteLink.href = '#';
    deleteLink.innerHTML = "Delete"
    deleteLink.onclick = function(){

        if (confirm("Are you sure?")){

            bjørneparkappen.adminconsole.species.delete(species);
        }
    }

    deleteCell.appendChild(deleteLink);

    // Set all cells except the delete cell to load the update page onclick
    for (i = 0; i < row.cells.length - 1; i++){

        row.cells[i].onclick = function(){

            // Load the update page with data
            bjørneparkappen.adminconsole.species.detail.loadUpdatePage(species);
        }
    }
};

// Filters Species table
bjørneparkappen.adminconsole.species.search = function(){

    var search = document.getElementById("list_species_search");
    var table = document.getElementById("list_species_table");

    bjørneparkappen.adminconsole.lookup(search, table);
}

// Deletes a Species
bjørneparkappen.adminconsole.species.delete = function(species){

    alert(species.id);
}


// Species Detail Page and elements
var speciesDetailPage = document.getElementById('species_detail_page');
var speciesDetailTitle = document.getElementById('species_detail_title');
var speciesDetailConfirm = document.getElementById('species_detail_confirm');
var speciesDetailCancel = document.getElementById('species_detail_cancel');
speciesDetailCancel.onclick = function(){

    // Display List Species page
    bjørneparkappen.adminconsole.navigation.displayPage(listSpeciesPage);
}
var speciesDetailForm = document.getElementById('species_detail_form');
var speciesCommonNameInput = document.getElementById('species_name');
var speciesLatinInput = document.getElementById('species_latin');
var speciesDescriptionInput = document.getElementById('species_description');

// Clears the Species Detail page
bjørneparkappen.adminconsole.species.detail.clearPageData = function(){

    // Clear form
    speciesDetailForm.reset();
};

// Load the Create Species page
bjørneparkappen.adminconsole.species.detail.loadCreatePage = function(){

    // Clear page data
    bjørneparkappen.adminconsole.species.detail.clearPageData();

    // Set page title and confirm button to create
    speciesDetailTitle.innerHTML = "Create Species";
    speciesDetailConfirm.value = "Create";

    // Display the detail page
    bjørneparkappen.adminconsole.navigation.displayPage(speciesDetailPage);
}

// Loads the Update Species page
bjørneparkappen.adminconsole.species.detail.loadUpdatePage = function(species){

    // Clear page data
    bjørneparkappen.adminconsole.species.detail.clearPageData();

    // Set page title and confirm button to update
    speciesDetailTitle.innerHTML = "Update Species";
    speciesDetailConfirm.value = "Save";

    // Populate form with new data
    speciesCommonNameInput.value = species.common_name;
    speciesLatinInput.value = species.latin;
    speciesDescriptionInput.value = species.description;

    // Display the detail page
    bjørneparkappen.adminconsole.navigation.displayPage(speciesDetailPage);
}



/** Areas */
var amenities = [];
var enclosures = [];

// Navigation Item
var areasNav = document.getElementById('areas');
areasNav.onclick = function(){

    // Select Areas nav item
    bjørneparkappen.adminconsole.navigation.selectNavItem(areasNav);

    // Load List Areas page
    bjørneparkappen.adminconsole.areas.list.loadPage();
};


// List Areas Page and elements
var listAreasPage = document.getElementById('list_areas_page');
var createAreaButton = document.getElementById('list_areas_create');
createAreaButton.onclick = function(){

    // Load the Create Area page
    bjørneparkappen.adminconsole.areas.detail.loadCreatePage();

    // Display Create Area page
    bjørneparkappen.adminconsole.navigation.displayPage(areaDetailPage);
}

// Clears the Areas page
bjørneparkappen.adminconsole.areas.list.clearPageData = function(){

    // Clear search bar
    document.getElementById('list_areas_search').value = "";

    // Retrieve table to empty
    var table = document.getElementById('list_areas_table');

    // Empty table
    while(table.rows.length > 1) {

        table.deleteRow(1);
    }
};

// Loads the List Areas page
bjørneparkappen.adminconsole.areas.list.loadPage = function(){

    // Clear page data
    bjørneparkappen.adminconsole.areas.list.clearPageData();

    // If no amenities or enclosures, retrieve from server
    if (amenities.length > 0 || enclosures.length > 0){

        // If amenities returned
        if (amenities.length > 0) {

            // Iterate through each one and add it to the table
            for (var i = 0; i < amenities.length; i++) {

                // Add event to table
                bjørneparkappen.adminconsole.areas.addToTable(amenities[i]);
            }
        }

        // If enclosures returned
        if (enclosures.length > 0) {

            // Iterate through each one and add it to the table
            for (var i = 0; i < enclosures.length; i++) {

                // Add event to table
                bjørneparkappen.adminconsole.areas.addToTable(enclosures[i]);
            }

        }

    } else {

        // Retrieve the list of areas
        bjørneparkappen.adminconsole.api.getAreas();
    }

    // Display the page
    bjørneparkappen.adminconsole.navigation.displayPage(listAreasPage);
};

// Adds a row to the Areas table
bjørneparkappen.adminconsole.areas.addToTable = function(area){

    var table = document.getElementById("list_areas_table");

    // Create new table row
    var row = table.insertRow();

    // Create cells to insert into the table
    var labelCell = row.insertCell(0);
    var typeCell = row.insertCell(1);
    var deleteCell = row.insertCell(2);

    // Provide values to the new row
    labelCell.innerHTML = area.label;

    if (area.amenity_type != null){

        typeCell.innerHTML = area.amenity_type;

    } else {

        typeCell.innerHTML = "ENCLOSURE";
    }

    // Create link to delete item
    var deleteLink = document.createElement('a');
    deleteLink.href = '#';
    deleteLink.innerHTML = "Delete"
    deleteLink.onclick = function(){

        if (confirm("Are you sure?")){

            bjørneparkappen.adminconsole.areas.delete(area);
        }
    }

    deleteCell.appendChild(deleteLink);

    // Set all cells except the delete cell to load the update page onclick
    for (i = 0; i < row.cells.length - 1; i++){

        row.cells[i].onclick = function(){

            // Load the update page with data
            bjørneparkappen.adminconsole.areas.detail.loadUpdatePage(area);
        }
    }
};

// Filters Areas table
bjørneparkappen.adminconsole.areas.search = function(){

    var search = document.getElementById("list_areas_search");
    var table = document.getElementById("list_areas_table");

    bjørneparkappen.adminconsole.lookup(search, table);
}

// Deletes an Area
bjørneparkappen.adminconsole.areas.delete = function(area){

    alert(area.id);
}


// Area Detail Page and elements
var areaDetailPage = document.getElementById('area_detail_page');
var areaDetailTitle = document.getElementById('area_detail_title');
var areaDetailConfirm = document.getElementById('area_detail_confirm');
var areaDetailCancel = document.getElementById('area_detail_cancel');
areaDetailCancel.onclick = function(){

    // Display List Areas page
    bjørneparkappen.adminconsole.navigation.displayPage(listAreasPage);
}
var areaTypeSelector = document.getElementById('area_type_selector');
areaTypeSelector.onchange = function(){

    var amenity_fields = document.getElementById('amenity_fields');

    if (areaTypeSelector.value == "enclosure"){

        amenity_fields.style.display = HIDDEN;

    } else {

        amenity_fields.style.display = DISPLAYED;
    }
}
var areaDetailForm = document.getElementById('area_detail_form');
var areaLabelInput = document.getElementById('area_label');
var areaVisitorDestinationInput = document.getElementById('area_visitor_destination');
var amenityTypeInput = document.getElementById('amenity_type');
var amenityDescriptionInput = document.getElementById('amenity_description');
var amenityImage = document.getElementById('amenity_image');

// Clears the Area Detail page
bjørneparkappen.adminconsole.areas.detail.clearPageData = function(){

    // Clear form
    areaDetailForm.reset();
};

// Load the Create Area page
bjørneparkappen.adminconsole.areas.detail.loadCreatePage = function(){

    // Clear page data
    bjørneparkappen.adminconsole.areas.detail.clearPageData();

    // Set page title and confirm button to create
    areaDetailTitle.innerHTML = "Create Area";
    areaDetailConfirm.value = "Create";

    // Display the detail page
    bjørneparkappen.adminconsole.navigation.displayPage(areaDetailPage);
}

// Loads the Update Area page
bjørneparkappen.adminconsole.areas.detail.loadUpdatePage = function(area){

    // Clear page data
    bjørneparkappen.adminconsole.areas.detail.clearPageData();

    // Set page title and confirm button to update
    areaDetailTitle.innerHTML = "Update Area";
    areaDetailConfirm.value = "Save";

    if (area.amenity_type != null){

        areaTypeSelector.value = "amenity";
        amenity_fields.style.display = DISPLAYED;

        amenityTypeInput.value = area.amenity_type;
        amenityDescriptionInput.value = area.description;

    } else {

        areaTypeSelector.value = "enclosure";
        amenity_fields.style.display = HIDDEN;
    }

    // Populate form with new data
    areaLabelInput.value = area.label;
    areaVisitorDestinationInput.value = area.visitor_destination;

    // Display the detail page
    bjørneparkappen.adminconsole.navigation.displayPage(areaDetailPage);
}



/** Animals */
var animals = [];

// Navigation Item
var animalsNav = document.getElementById('animals');
animalsNav.onclick = function(){

    // Select Animals nav item
    bjørneparkappen.adminconsole.navigation.selectNavItem(animalsNav);

    // Load List Animals page
    bjørneparkappen.adminconsole.animals.list.loadPage();
};


// List Animals Page and elements
var listAnimalsPage = document.getElementById('list_animals_page');
var createAnimalButton = document.getElementById('list_animals_create');
createAnimalButton.onclick = function(){

    // Load the Create Animal page
    bjørneparkappen.adminconsole.animals.detail.loadCreatePage();

    // Display Create Animal page
    bjørneparkappen.adminconsole.navigation.displayPage(animalDetailPage);
}

// Clears the List Animals page
bjørneparkappen.adminconsole.animals.list.clearPageData = function(){

    // Clear search bar
    document.getElementById('list_animals_search').value = "";

    // Retrieve table to empty
    var table = document.getElementById('list_animals_table');

    // Empty table
    while(table.rows.length > 1) {

        table.deleteRow(1);
    }
};

// Loads the List Animals page
bjørneparkappen.adminconsole.animals.list.loadPage = function(){

    // Clear page data
    bjørneparkappen.adminconsole.animals.list.clearPageData();

    // If animals returned
    if (animals.length > 0) {

        // Iterate through each one and display it
        for (var i = 0; i < animals.length; i++) {

            // Add animal to table
            bjørneparkappen.adminconsole.animals.addToTable(animals[i]);
        }

    } else {

        // Retrieve the list of animals
        bjørneparkappen.adminconsole.api.getAnimals();
    }

    // Display the page
    bjørneparkappen.adminconsole.navigation.displayPage(listAnimalsPage);
};

// Adds a row to the Animals table
bjørneparkappen.adminconsole.animals.addToTable = function(animal){

    var table = document.getElementById("list_animals_table");

    // Create new table row
    var row = table.insertRow();

    // Create cells to insert into the table
    var nameCell = row.insertCell(0);
    var speciesCell = row.insertCell(1);
    var availableCell = row.insertCell(2);
    var deleteCell = row.insertCell(3);

    // Provide values to the new row
    nameCell.innerHTML = animal.name;
    speciesCell.innerHTML = animal.species.common_name;

    var checkbox = document.createElement('input');
    checkbox.type = "checkbox";
    checkbox.checked = animal.is_available;
    availableCell.appendChild(checkbox);

    // Create link to delete item
    var deleteLink = document.createElement('a');
    deleteLink.href = '#';
    deleteLink.innerHTML = "Delete"
    deleteLink.onclick = function(){

        if (confirm("Are you sure?")){

            bjørneparkappen.adminconsole.animals.delete(animal);
        }
    }

    deleteCell.appendChild(deleteLink);

    // Set all cells except the delete and available cells to load the update page onclick
    for (i = 0; i < row.cells.length - 2; i++){

        row.cells[i].onclick = function(){

            // Load the update page with data
            bjørneparkappen.adminconsole.animals.detail.loadUpdatePage(animal);
        }
    }
};

// Filters Animals table
bjørneparkappen.adminconsole.animals.search = function(){

    var search = document.getElementById("list_animals_search");
    var table = document.getElementById("list_animals_table");

    bjørneparkappen.adminconsole.lookup(search, table);
}

// Deletes an Animal
bjørneparkappen.adminconsole.animals.delete = function(animal){

    alert(animal.id);
}

// Animal Detail Page and elements
var animalDetailPage = document.getElementById('animal_detail_page');
var animalDetailTitle = document.getElementById('animal_detail_title');
var animalDetailConfirm = document.getElementById('animal_detail_confirm');
var animalDetailCancel = document.getElementById('animal_detail_cancel');
animalDetailCancel.onclick = function(){

     // Display List Animals page
     bjørneparkappen.adminconsole.navigation.displayPage(listAnimalsPage);
}
var animalDetailForm = document.getElementById('animal_detail_form');
var animalNameInput = document.getElementById('animal_name');
var animalDescriptionInput = document.getElementById('animal_description');
var animalSpeciesInput = document.getElementById('animal_species');
var animalAvailableInput = document.getElementById('animal_available');

// Clears the Animal Detail page
bjørneparkappen.adminconsole.animals.detail.clearPageData = function(){

    // Clear the form
    animalDetailForm.reset();

    // Clear species dropdown
    animalSpeciesInput.length = 0;

    // Add species options to dropdown
    for (i = 0; i < species.length; i++){

        var option = document.createElement("option");
        option.value = species[i].id;
        option.text = species[i].common_name;

        animalSpeciesInput.add(option);
    }
};

// Load the Create Animal page
bjørneparkappen.adminconsole.animals.detail.loadCreatePage = function(){

    // Clear page data
    bjørneparkappen.adminconsole.animals.detail.clearPageData();

    // Set page title and confirm button to create
    animalDetailTitle.innerHTML = "Create Animal";
    animalDetailConfirm.value = "Create";

    // Display the detail page
    bjørneparkappen.adminconsole.navigation.displayPage(animalDetailPage);
}

// Loads the Update Animal page
bjørneparkappen.adminconsole.animals.detail.loadUpdatePage = function(animal){

    // Clear page data
    bjørneparkappen.adminconsole.animals.detail.clearPageData();

    // Set page title and confirm button to update
    animalDetailTitle.innerHTML = "Update Animal";
    animalDetailConfirm.value = "Save";

    // Populate page with new data
    animalNameInput.value = animal.name;
    animalDescriptionInput.value = animal.description;
    animalSpeciesInput.value = animal.species.id;
    animalAvailableInput.checked = animal.is_available;

    // Display the detail page
    bjørneparkappen.adminconsole.navigation.displayPage(animalDetailPage);
}



/** Events */
var events = [];
var feedings = [];

// Navigation Item
var eventsNav = document.getElementById('events');
eventsNav.onclick = function(){

    // Select Events nav item
    bjørneparkappen.adminconsole.navigation.selectNavItem(eventsNav);

    // Load List Events page
    bjørneparkappen.adminconsole.events.list.loadPage();
};


// List Events Page and elements
var listEventsPage = document.getElementById('list_events_page');
var createEventButton = document.getElementById('list_events_create');
createEventButton.onclick = function(){

    // Load the Create Event page
    bjørneparkappen.adminconsole.events.detail.loadCreatePage();

    // Display Create Event page
    bjørneparkappen.adminconsole.navigation.displayPage(eventDetailPage);
}

// Clears the List Events page
bjørneparkappen.adminconsole.events.list.clearPageData = function(){

    // Clear search bar
    document.getElementById('list_events_search').value = "";

    // Retrieve table to empty
    var table = document.getElementById('list_events_table');

    // Empty table
    while(table.rows.length > 1) {

        table.deleteRow(1);
    }
};

// Loads the List Events page
bjørneparkappen.adminconsole.events.list.loadPage = function(){

    // Clear page data
    bjørneparkappen.adminconsole.events.list.clearPageData();

    if (events.length > 0){

        // If events returned
        if (events.length > 0) {

            // Iterate through each one and display it
            for (var i = 0; i < events.length; i++) {

                // Add event to table
                bjørneparkappen.adminconsole.events.addToTable(events[i]);
            }
        }

        // If feedings returned
        if (feedings.length > 0) {

            // Iterate through each one and display it
            for (var i = 0; i < feedings.length; i++) {

                // Add event to table
                bjørneparkappen.adminconsole.events.addToTable(feedings[i]);
            }

        }

    } else {

        // Retrieve the list of events
        bjørneparkappen.adminconsole.api.getEvents();
    }

    // Display the page
    bjørneparkappen.adminconsole.navigation.displayPage(listEventsPage);
};

// Adds a row to the Events table
bjørneparkappen.adminconsole.events.addToTable = function(event){

    var table = document.getElementById("list_events_table");

    // Create new table row
    var row = table.insertRow();

    // Create cells to insert into the table
    var labelCell = row.insertCell(0);
    var startTimeCell = row.insertCell(1);
    var endTimeCell = row.insertCell(2);
    var keeperCell = row.insertCell(3);
    var activeCell = row.insertCell(4);
    var deleteCell = row.insertCell(5);

    // Provide values to the new row
    labelCell.innerHTML = event.label;
    startTimeCell.innerHTML = event.start_time;
    endTimeCell.innerHTML = event.end_time;

    if (event.keeper != null){

        keeperCell.innerHTML = event.keeper.name;

    } else {

        keeperCell.innerHTML = "-";
    }

    var checkbox = document.createElement('input');
    checkbox.type = "checkbox";
    checkbox.checked = event.is_active;
    activeCell.appendChild(checkbox);

    // Create link to delete item
    var deleteLink = document.createElement('a');
    deleteLink.href = '#';
    deleteLink.innerHTML = "Delete"
    deleteLink.onclick = function(){

        if (confirm("Are you sure?")){

            bjørneparkappen.adminconsole.events.delete(event);
        }
    }

    deleteCell.appendChild(deleteLink);

    // Set all cells except the delete and available cells to load the update page onclick
    for (i = 0; i < row.cells.length - 2; i++){

        row.cells[i].onclick = function(){

            // Load the update page with data
            bjørneparkappen.adminconsole.events.detail.loadUpdatePage(event);
        }
    }
};

// Filters Events table
bjørneparkappen.adminconsole.events.search = function(){

    var search = document.getElementById("list_events_search");
    var table = document.getElementById("list_events_table");

    bjørneparkappen.adminconsole.lookup(search, table);
}

// Deletes an Event
bjørneparkappen.adminconsole.events.delete = function(event){

    alert(event.id);
}


// Event Detail Page and elements
var eventDetailPage = document.getElementById('event_detail_page');
var eventDetailTitle = document.getElementById('event_detail_title');
var eventDetailConfirm = document.getElementById('event_detail_confirm');
var eventDetailCancel = document.getElementById('event_detail_cancel');
eventDetailCancel.onclick = function(){
    // Display List Events page
    bjørneparkappen.adminconsole.navigation.displayPage(listEventsPage);
}
var eventTypeSelector = document.getElementById('event_type_selector');
eventTypeSelector.onchange = function(){

    var feeding_fields = document.getElementById('feeding_fields');

    if (areaTypeSelector.value == "event"){

        feeding_fields.style.display = HIDDEN;

    } else {

        feeding_fields.style.display = DISPLAYED;
    }
}
var eventDetailForm = document.getElementById('event_detail_form');
var eventLabelInput = document.getElementById('event_label');
var eventStartTimeInput = document.getElementById('event_start_time');
var eventEndTimeInput = document.getElementById('event_end_time');
var eventAreaInput = document.getElementById('event_area');
var eventDescriptionInput = document.getElementById('event_description');
var eventKeeperInput = document.getElementById('event_keeper');
var eventActiveInput = document.getElementById('event_active');

// Clears the Event Detail page
bjørneparkappen.adminconsole.events.detail.clearPageData = function(){

    // Clear the form
    eventDetailForm.reset();

    // Clear areas dropdown
    eventAreaInput.length = 0;

    // Add amenity options to dropdown
    for (i = 0; i < amenities.length; i++){

        var option = document.createElement("option");
        option.value = amenities[i].id;
        option.text = amenities[i].label;

        eventAreaInput.add(option);
    }

    // Add enclosure options to dropdown
    for (i = 0; i < enclosures.length; i++){

        var option = document.createElement("option");
        option.value = enclosures[i].id;
        option.text = enclosures[i].label;

        eventAreaInput.add(option);
    }

    // Clear keepers dropdown
    eventKeeperInput.length = 0;

    // Add keeper options to dropdown
    for (i = 0; i < keepers.length; i++){

        var option = document.createElement("option");
        option.value = keepers[i].id;
        option.text = keepers[i].name;

        eventKeeperInput.add(option);
    }
};

// Load the Create Event page
bjørneparkappen.adminconsole.events.detail.loadCreatePage = function(){

    // Clear page data
    bjørneparkappen.adminconsole.events.detail.clearPageData();

    // Set page title and confirm button to create
    eventDetailTitle.innerHTML = "Create Event";
    eventDetailConfirm.value = "Create";

    // Display the detail page
    bjørneparkappen.adminconsole.navigation.displayPage(eventDetailPage);
}

// Loads the Update Event page
bjørneparkappen.adminconsole.events.detail.loadUpdatePage = function(event){

    // Clear page data
    bjørneparkappen.adminconsole.events.detail.clearPageData();

    // Set confirm button to update
    eventDetailTitle.innerHTML = "Update Event";
    eventDetailConfirm.value = "Save";

    // Populate page with new data
    eventLabelInput.value = event.label;
    eventStartTimeInput.value = event.start_time;
    eventEndTimeInput.value = event.end_time;
    eventAreaInput.value = event.location.id;
    eventDescriptionInput.value = event.description;

    // If item is a feeding
    if (event.keeper != null){

        eventTypeSelector.value = "feeding";
        feeding_fields.style.display = DISPLAYED;

        eventKeeperInput.value = event.keeper.id;

    } else {

        eventTypeSelector.value = "event";
        feeding_fields.style.display = HIDDEN;
    }

    eventActiveInput.checked = event.is_active;

    // Display the detail page
    bjørneparkappen.adminconsole.navigation.displayPage(eventDetailPage);
}



/** Keepers */
var keepers = [];

// Navigation Item
var keepersNav = document.getElementById('keepers');
keepersNav.onclick = function(){

    // Select Keepers nav item
    bjørneparkappen.adminconsole.navigation.selectNavItem(keepersNav);

    // Load List Keepers page
    bjørneparkappen.adminconsole.keepers.list.loadPage();
};

// List Keepers Page and elements
var listKeepersPage = document.getElementById('list_keepers_page');
var createKeeperButton = document.getElementById('list_keepers_create');
createKeeperButton.onclick = function(){

    // Load the Create Keeper page
    bjørneparkappen.adminconsole.keepers.detail.loadCreatePage();

    // Display Create Keeper page
    bjørneparkappen.adminconsole.navigation.displayPage(keeperDetailPage);
}

// Clears the List Keepers page
bjørneparkappen.adminconsole.keepers.list.clearPageData = function(){

    // Clear search bar
    document.getElementById('list_keepers_search').value = "";

    // Retrieve table to empty
    var table = document.getElementById('list_keepers_table');

    // Empty table
    while(table.rows.length > 1) {

        table.deleteRow(1);
    }
};

// Loads the List Keepers page
bjørneparkappen.adminconsole.keepers.list.loadPage = function(){

    // Clear page data
    bjørneparkappen.adminconsole.keepers.list.clearPageData();

    // For each keepers
    if (keepers.length > 0) {

        // Iterate through each one and add it to the table
        for (var i = 0; i < keepers.length; i++) {

            // Add keeper to table
            bjørneparkappen.adminconsole.keepers.addToTable(keepers[i]);
        }

    } else {

        // Retrieve list of keepers
        bjørneparkappen.adminconsole.api.getKeepers();
    }

    // Display the page
    bjørneparkappen.adminconsole.navigation.displayPage(listKeepersPage);
};

// Adds a row to the Keepers table
bjørneparkappen.adminconsole.keepers.addToTable = function(keeper){

    var table = document.getElementById("list_keepers_table");

    // Create new table row
    var row = table.insertRow();

    // Create cells to insert into the table
    var nameCell = row.insertCell(0);
    var bioCell = row.insertCell(1);
    var deleteCell = row.insertCell(2);

    // Provide values to the new row
    nameCell.innerHTML = keeper.name;
    bioCell.innerHTML = keeper.bio;

    // Create link to delete item
    var deleteLink = document.createElement('a');
    deleteLink.href = '#';
    deleteLink.innerHTML = "Delete"
    deleteLink.onclick = function(){

        if (confirm("Are you sure?")){

            bjørneparkappen.adminconsole.keepers.delete(keeper);
        }
    }

    deleteCell.appendChild(deleteLink);

    // Set all cells except the delete cell to load the update page onclick
    for (i = 0; i < row.cells.length - 1; i++){

        row.cells[i].onclick = function(){

            // Load the update page with data
            bjørneparkappen.adminconsole.keepers.detail.loadUpdatePage(keeper);
        }
    }
};

// Filters Keepers table
bjørneparkappen.adminconsole.keepers.search = function(){

    var search = document.getElementById("list_keepers_search");
    var table = document.getElementById("list_keepers_table");

    bjørneparkappen.adminconsole.lookup(search, table);
}

// Deletes a Keeper
bjørneparkappen.adminconsole.keepers.delete = function(keeper){

    alert(keeper.id);
}

// Keeper Detail Page and elements
var keeperDetailPage = document.getElementById('keeper_detail_page');
var keeperDetailTitle = document.getElementById('keeper_detail_title');
var keeperDetailConfirm = document.getElementById('keeper_detail_confirm');
var keeperDetailCancel = document.getElementById('keeper_detail_cancel');
keeperDetailCancel.onclick = function(){

    // Display List Keepers page
    bjørneparkappen.adminconsole.navigation.displayPage(listKeepersPage);
}
var keeperDetailForm = document.getElementById('keeper_detail_form');
var keeperNameInput = document.getElementById('keeper_name');
var keeperBioInput = document.getElementById('keeper_bio');

// Clears the Keeper Detail page
bjørneparkappen.adminconsole.keepers.detail.clearPageData = function(){

    // Clear the form
    keeperDetailForm.reset();
};

// Load the Create Keeper page
bjørneparkappen.adminconsole.keepers.detail.loadCreatePage = function(){

    // Clear page data
    bjørneparkappen.adminconsole.keepers.detail.clearPageData();

    // Set page title and confirm button to create
    keeperDetailTitle.innerHTML = "Create Keeper";
    keeperDetailConfirm.value = "Create";

    // Display the detail page
    bjørneparkappen.adminconsole.navigation.displayPage(keeperDetailPage);
}

// Loads the Update Keeper page
bjørneparkappen.adminconsole.keepers.detail.loadUpdatePage = function(keeper){

    // Set page title and confirm button to update
    keeperDetailTitle.innerHTML = "Update Keeper";
    keeperDetailConfirm.value = "Save";

    keeperNameInput.value = keeper.name;
    keeperBioInput.value = keeper.bio;

    // Display the detail page
    bjørneparkappen.adminconsole.navigation.displayPage(keeperDetailPage);
}
