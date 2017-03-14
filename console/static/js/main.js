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
bjørneparkappen.adminconsole.areas = bjørneparkappen.adminconsole.areas || {};
bjørneparkappen.adminconsole.animals = bjørneparkappen.adminconsole.animals || {};
bjørneparkappen.adminconsole.events = bjørneparkappen.adminconsole.events || {};
bjørneparkappen.adminconsole.keepers = bjørneparkappen.adminconsole.keepers || {};

// To be used in setting the currently selected nav item
const ACTIVE = "active";

// To be used in setting the currently displayed page
const DISPLAYED = "block";
const HIDDEN = "none";

// Element to display whenever a operation requires the user to wait
var loadingScreen = document.getElementById('loading-screen');
var loadingText = document.getElementById('loading-text');

// Puts the application UI into a wait state
bjørneparkappen.adminconsole.startWait = function(){

    bjørneparkappen.adminconsole.navigation.hideAllPages();

    loadingText.style.display = DISPLAYED;
};

// Terminates the application UI wait state
bjørneparkappen.adminconsole.endWait = function(){

    loadingText.style.display = HIDDEN;
};

// Initialises the application
bjørneparkappen.adminconsole.init = function() {

    // Load Species page if so
    bjørneparkappen.adminconsole.species.loadPage();
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

    createSpeciesPage.style.display = HIDDEN;
    updateSpeciesPage.style.display = HIDDEN;
    listSpeciesPage.style.display = HIDDEN;
    createAreaPage.style.display = HIDDEN;
    updateAreaPage.style.display = HIDDEN;
    listAreasPage.style.display = HIDDEN;
    createAnimalPage.style.display = HIDDEN;
    updateAnimalPage.style.display = HIDDEN;
    listAnimalsPage.style.display = HIDDEN;
    createEventPage.style.display = HIDDEN;
    updateEventPage.style.display = HIDDEN;
    listEventsPage.style.display = HIDDEN;
    createKeeperPage.style.display = HIDDEN;
    updateKeeperPage.style.display = HIDDEN;
    listKeepersPage.style.display = HIDDEN;
};

// Displays a single page
bjørneparkappen.adminconsole.navigation.displayPage = function(page){

    // Hide all pages
    bjørneparkappen.adminconsole.navigation.hideAllPages();

    // Display selected page
    page.style.display = DISPLAYED;
};

// Nav items
var speciesNav = document.getElementById('species');
var areasNav = document.getElementById('areas');
var animalsNav = document.getElementById('animals');
var eventsNav = document.getElementById('events');
var keepersNav = document.getElementById('keepers');

// Species Menu nav item onclick event
speciesNav.onclick = function(){

    // Clear page data
    bjørneparkappen.adminconsole.species.clearPageData();

    // List all species once page has loaded
    bjørneparkappen.adminconsole.api.listSpecies();

    // Select Species nav item
    bjørneparkappen.adminconsole.navigation.selectNavItem(speciesNav);

    // Display List Species page
    bjørneparkappen.adminconsole.navigation.displayPage(listSpeciesPage);
}

// Areas nav item onclick event
areasNav.onclick = function(){

    // Clear page data
    bjørneparkappen.adminconsole.areas.clearPageData();

    // List all species once page has loaded
    bjørneparkappen.adminconsole.api.listAreas();

    // Select Areas nav item
    bjørneparkappen.adminconsole.navigation.selectNavItem(areasNav);

    // Display List Areas page
    bjørneparkappen.adminconsole.navigation.displayPage(listAreasPage);
};

// Animals nav item onclick event
animalsNav.onclick = function(){

    // Clear page data
    bjørneparkappen.adminconsole.animals.clearPageData();

    // List all animals once page has loaded
    bjørneparkappen.adminconsole.api.listAnimals();

    // Select Animals nav item
    bjørneparkappen.adminconsole.navigation.selectNavItem(animalsNav);

    // Display List Animals page
    bjørneparkappen.adminconsole.navigation.displayPage(listAnimalsPage);
};

// Events nav item onclick event
eventsNav.onclick = function(){

    // Clear page data
    bjørneparkappen.adminconsole.events.clearPageData();

    // List all events once page has loaded
    bjørneparkappen.adminconsole.api.listEvents();

    // Select Events nav item
    bjørneparkappen.adminconsole.navigation.selectNavItem(eventsNav);

    // Display List Events page
    bjørneparkappen.adminconsole.navigation.displayPage(listEventsPage);
};

// Keepers nav item onclick event
keepersNav.onclick = function(){

    // Clear page data
    bjørneparkappen.adminconsole.keepers.clearPageData();

    // List all keepers once page has loaded
    bjørneparkappen.adminconsole.api.listKeepers();

    // Select Keepers nav item
    bjørneparkappen.adminconsole.navigation.selectNavItem(keepersNav);

    // Display List Keepers page
    bjørneparkappen.adminconsole.navigation.displayPage(listKeepersPage);
};

// Pages
var createSpeciesPage = document.getElementById('create-species-page');
var updateSpeciesPage = document.getElementById('update-species-page');
var listSpeciesPage = document.getElementById('list-species-page');
var createAreaPage = document.getElementById('create-area-page');
var updateAreaPage = document.getElementById('update-area-page');
var listAreasPage = document.getElementById('list-areas-page');
var createAnimalPage = document.getElementById('create-animal-page');
var updateAnimalPage = document.getElementById('update-animal-page');
var listAnimalsPage = document.getElementById('list-animals-page');
var createEventPage = document.getElementById('create-event-page');
var updateEventPage = document.getElementById('update-event-page');
var listEventsPage = document.getElementById('list-events-page');
var createKeeperPage = document.getElementById('create-keeper-page');
var updateKeeperPage = document.getElementById('update-keeper-page');
var listKeepersPage = document.getElementById('list-keepers-page');

// Clears the Species page
bjørneparkappen.adminconsole.species.clearPageData = function(){};

// Clears the Areas page
bjørneparkappen.adminconsole.areas.clearPageData = function(){};

// Clears the Animals page
bjørneparkappen.adminconsole.animals.clearPageData = function(){};

// Clears the Events page
bjørneparkappen.adminconsole.events.clearPageData = function(){};

// Clears the Keepers page
bjørneparkappen.adminconsole.keepers.clearPageData = function(){};

// Loads the Species page
bjørneparkappen.adminconsole.species.loadPage = function(){

    // Retrieve the list of species
    bjørneparkappen.adminconsole.api.listSpecies();
};

// Adds a row to the Species table
bjørneparkappen.adminconsole.species.addToTable = function(id, commonName, latin, description, image){

    var table = document.getElementById("species-table");

    // Create new table row
    var row = table.insertRow();

    // Create cells to insert into the table
    var idCell = row.insertCell(-1);
    var commonNameCell = row.insertCell(1);
    var latinCell = row.insertCell(2);
    var descriptionCell = row.insertCell(3);
    var deleteCell = row.insertCell(4);

    // Provide values to the new row
    idCell.innerHTML = id;
    idCell.style.display = HIDDEN;
    commonNameCell.innerHTML = commonName;
    latinCell.innerHTML = latin;
    descriptionCell.innerHTML = description;
    descriptionCell.style.display = HIDDEN;
    deleteCell.innerHTML = "<a href='#'>Delete</a>"
};

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

// Filter Species table
bjørneparkappen.adminconsole.species.search = function(){

    var search = document.getElementById("species-search");
    var table = document.getElementById("species-table");

    bjørneparkappen.adminconsole.lookup(search, table);
}

// Filter Areas table
bjørneparkappen.adminconsole.areas.search = function(){

    var search = document.getElementById("area-search");
    var table = document.getElementById("areas-table");

    bjørneparkappen.adminconsole.lookup(search, table);
}

// Filter Animals table
bjørneparkappen.adminconsole.animals.search = function(){

    var search = document.getElementById("animal-search");
    var table = document.getElementById("animals-table");

    bjørneparkappen.adminconsole.lookup(search, table);
}

// Filter Events table
bjørneparkappen.adminconsole.events.search = function(){

    var search = document.getElementById("event-search");
    var table = document.getElementById("events-table");

    bjørneparkappen.adminconsole.lookup(search, table);
}

// Filter Keepers table
bjørneparkappen.adminconsole.keepers.search = function(){

    var search = document.getElementById("keeper-search");
    var table = document.getElementById("keepers-table");

    bjørneparkappen.adminconsole.lookup(search, table);
}

// Create buttons
var createSpeciesButton = document.getElementById('species-create');
var createAreaButton = document.getElementById('area-create');
var createAnimalButton = document.getElementById('animal-create');
var createEventButton = document.getElementById('event-create');
var createKeeperButton = document.getElementById('keeper-create');

// Create Species onclick event
createSpeciesButton.onclick = function(){

    // Clear page data
    bjørneparkappen.adminconsole.species.clearPageData();

    // Display Create Species page
    bjørneparkappen.adminconsole.navigation.displayPage(createSpeciesPage);
}

// Create Area onclick event
createAreaButton.onclick = function(){

    // Clear page data
    bjørneparkappen.adminconsole.areas.clearPageData();

    // Display Create Area page
    bjørneparkappen.adminconsole.navigation.displayPage(createAreaPage);
}

// Create Animal onclick event
createAnimalButton.onclick = function(){

    // Clear page data
    bjørneparkappen.adminconsole.animals.clearPageData();

    // Display Create Animal page
    bjørneparkappen.adminconsole.navigation.displayPage(createAnimalPage);
}

// Create Event onclick event
createEventButton.onclick = function(){

    // Clear page data
    bjørneparkappen.adminconsole.events.clearPageData();

    // Display Create Event page
    bjørneparkappen.adminconsole.navigation.displayPage(createEventPage);
}

// Create Keeper onclick event
createKeeperButton.onclick = function(){

    // Clear page data
    bjørneparkappen.adminconsole.keepers.clearPageData();

    // Display Create Keeper page
    bjørneparkappen.adminconsole.navigation.displayPage(createKeeperPage);
}

// Cancel buttons
var cancelSpeciesButton = document.getElementById('species-create-cancel');
var cancelAreaButton = document.getElementById('area-create-cancel');
var cancelAnimalButton = document.getElementById('animal-create-cancel');
var cancelEventButton = document.getElementById('event-create-cancel');
var cancelKeeperButton = document.getElementById('keeper-create-cancel');

// Cancel Species onclick event
cancelSpeciesButton.onclick = function(){

    // Display List Species page
    bjørneparkappen.adminconsole.navigation.displayPage(listSpeciesPage);
}

// Cancel Area onclick event
cancelAreaButton.onclick = function(){

    // Display List Areas page
    bjørneparkappen.adminconsole.navigation.displayPage(listAreasPage);
}

// Cancel Animal onclick event
cancelAnimalButton.onclick = function(){

     // Display List Animals page
     bjørneparkappen.adminconsole.navigation.displayPage(listAnimalsPage);
}

// Cancel Event onclick event
cancelEventButton.onclick = function(){
    // Display List Events page
    bjørneparkappen.adminconsole.navigation.displayPage(listEventsPage);
}

// Cancel Keeper onclick event
cancelKeeperButton.onclick = function(){

    // Display List Keepers page
    bjørneparkappen.adminconsole.navigation.displayPage(listKeepersPage);
}

// Selectors
var areaTypeSelector = document.getElementById('area_type_selector');

areaTypeSelector.onchange = function(){

    var amenity_fields = document.getElementById('amenity_fields');

    if (areaTypeSelector.value == "enclosure"){

        amenity_fields.style.display = HIDDEN;

    } else {

        amenity_fields.style.display = DISPLAYED;
    }
}


// API
bjørneparkappen.adminconsole.api.listSpecies = function(){

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
                resp.species = resp.species || [];

                // If species returned
                if (resp.species.length > 0) {

                    // Iterate through each one and display it
                    for (var i = 0; i < resp.species.length; i++) {

                        var id = resp.species[i].id;
                        var commonName = resp.species[i].common_name;
                        var latin = resp.species[i].latin;
                        var description = resp.species[i].description;
                        var image = resp.species[i].image;

                        // Add species to table
                        bjørneparkappen.adminconsole.species.addToTable(id, commonName, latin, description, image);
                    }

                }
            }

            // Terminate UI wait state
            bjørneparkappen.adminconsole.endWait();
        }
    };

    // Send request
    xhr.send();
};

bjørneparkappen.adminconsole.api.listAreas = function(){};
bjørneparkappen.adminconsole.api.listAnimals = function(){};
bjørneparkappen.adminconsole.api.listEvents = function(){};
bjørneparkappen.adminconsole.api.listKeepers = function(){};
