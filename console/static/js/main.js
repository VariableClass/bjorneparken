/**
 @fileoverview
 Provides methods to be used across the Bjørneparkappen Administrative Console
 */

/** Main */

// Base REST API request URL
const BASE_URL = "https://api-dot-bjorneparkappen.appspot.com/_ah/api/bjorneparkappen_api/v1.0/";

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

    loadingScreen.style.display = DISPLAYED;
    loadingText.style.display = DISPLAYED;
};

// Terminates the application UI wait state
bjørneparkappen.adminconsole.endWait = function(){

    loadingScreen.style.display = HIDDEN;
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

// Filter species table
bjørneparkappen.adminconsole.species.search = function(){

    var search = document.getElementById("species-search");
    var table = document.getElementById("species-table");

    bjørneparkappen.adminconsole.lookup(search, table);
}

// API
bjørneparkappen.adminconsole.api.listSpecies = function(){};
bjørneparkappen.adminconsole.api.listAreas = function(){};
bjørneparkappen.adminconsole.api.listAnimals = function(){};
bjørneparkappen.adminconsole.api.listEvents = function(){};
bjørneparkappen.adminconsole.api.listKeepers = function(){};
