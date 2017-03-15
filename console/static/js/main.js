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
bjørneparkappen.adminconsole.species.create = bjørneparkappen.adminconsole.species.create || {};
bjørneparkappen.adminconsole.species.update = bjørneparkappen.adminconsole.species.update || {};
bjørneparkappen.adminconsole.areas = bjørneparkappen.adminconsole.areas || {};
bjørneparkappen.adminconsole.areas.list = bjørneparkappen.adminconsole.areas.list || {};
bjørneparkappen.adminconsole.areas.create = bjørneparkappen.adminconsole.areas.create || {};
bjørneparkappen.adminconsole.areas.update = bjørneparkappen.adminconsole.areas.update || {};
bjørneparkappen.adminconsole.animals = bjørneparkappen.adminconsole.animals || {};
bjørneparkappen.adminconsole.animals.list = bjørneparkappen.adminconsole.animals.list || {};
bjørneparkappen.adminconsole.animals.create = bjørneparkappen.adminconsole.animals.create || {};
bjørneparkappen.adminconsole.animals.update = bjørneparkappen.adminconsole.animals.update || {};
bjørneparkappen.adminconsole.events = bjørneparkappen.adminconsole.events || {};
bjørneparkappen.adminconsole.events.list = bjørneparkappen.adminconsole.events.list || {};
bjørneparkappen.adminconsole.events.create = bjørneparkappen.adminconsole.events.create || {};
bjørneparkappen.adminconsole.events.update = bjørneparkappen.adminconsole.events.update || {};
bjørneparkappen.adminconsole.keepers = bjørneparkappen.adminconsole.keepers || {};
bjørneparkappen.adminconsole.keepers.list = bjørneparkappen.adminconsole.keepers.list || {};
bjørneparkappen.adminconsole.keepers.create = bjørneparkappen.adminconsole.keepers.create || {};
bjørneparkappen.adminconsole.keepers.update = bjørneparkappen.adminconsole.keepers.update || {};

// To be used in setting the currently selected nav item
const ACTIVE = "active";

// To be used in setting the currently displayed page
const DISPLAYED = "block";
const HIDDEN = "none";

// Element to display whenever a operation requires the user to wait
var loadingText = document.getElementById('loading-text');

// Puts the application UI into a wait state
bjørneparkappen.adminconsole.startWait = function(){

    loadingText.style.display = DISPLAYED;
};

// Terminates the application UI wait state
bjørneparkappen.adminconsole.endWait = function(){

    loadingText.style.display = HIDDEN;
};

// Initialises the application
bjørneparkappen.adminconsole.init = function() {

    // Load Species page if so
    bjørneparkappen.adminconsole.species.list.loadPage();
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



/** Species */

// Navigation Item
var speciesNav = document.getElementById('species');
speciesNav.onclick = function(){

    // Clear page data
    bjørneparkappen.adminconsole.species.list.clearPageData();

    // Select Species nav item
    bjørneparkappen.adminconsole.navigation.selectNavItem(speciesNav);

    // Load Species list page
    bjørneparkappen.adminconsole.species.list.loadPage();

    // Display List Species page
    bjørneparkappen.adminconsole.navigation.displayPage(listSpeciesPage);
}


// List Species Page and elements
var listSpeciesPage = document.getElementById('list_species_page');
var createSpeciesButton = document.getElementById('list_species_create');
createSpeciesButton.onclick = function(){

    // Clear Create Species page data
    bjørneparkappen.adminconsole.species.create.clearPageData();

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

// Loads the Species List page
bjørneparkappen.adminconsole.species.list.loadPage = function(){

    // Retrieve the list of species
    bjørneparkappen.adminconsole.api.listSpecies();

    // Display the update page
    bjørneparkappen.adminconsole.navigation.displayPage(listSpeciesPage);
};

// Adds a row to the Species table
bjørneparkappen.adminconsole.species.addToTable = function(id, commonName, latin, description, image){

    var table = document.getElementById("list_species_table");

    // Create new table row
    var row = table.insertRow();

    // Create cells to insert into the table
    var idCell = row.insertCell(0);
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

    row.onclick = function(){

        // Load the update page with data
        bjørneparkappen.adminconsole.species.update.loadPage(id, commonName, latin, description);
    }
};

// Filter Species table
bjørneparkappen.adminconsole.species.search = function(){

    var search = document.getElementById("list_species_search");
    var table = document.getElementById("list_species_table");

    bjørneparkappen.adminconsole.lookup(search, table);
}


// Species Detail Page and elements
var speciesDetailPage = document.getElementById('species_detail_page');
var cancelSpeciesButton = document.getElementById('species_detail_cancel');
cancelSpeciesButton.onclick = function(){

    // Display List Species page
    bjørneparkappen.adminconsole.navigation.displayPage(listSpeciesPage);
}

// Clears the Species Detail page
bjørneparkappen.adminconsole.species.update.clearPageData = function(){

    // TODO Implement
};

// Loads the Update Species page
bjørneparkappen.adminconsole.species.update.loadPage = function(id, commonName, latin, description){

    // Set update page title
    var updateSpeciesTitle = document.getElementById('species_detail_title');
    updateSpeciesTitle.innerHTML = "Update Species";

    // Set update page confim button text
    var updateSpeciesButton = document.getElementById('species_detail_confirm');
    updateSpeciesButton.value = "Save";

    // Populate form with new data
    var commonNameInput = document.getElementById('species_name');
    var latinInput = document.getElementById('species_latin');
    var descriptionInput = document.getElementById('species_description');

    commonNameInput.value = commonName;
    latinInput.value = latin;
    descriptionInput.value = description;

    // Display the update page
    bjørneparkappen.adminconsole.navigation.displayPage(speciesDetailPage);
}



/** Areas */

// Navigation Item
var areasNav = document.getElementById('areas');
areasNav.onclick = function(){

    // Clear page data
    bjørneparkappen.adminconsole.areas.clearPageData();

    // Select Areas nav item
    bjørneparkappen.adminconsole.navigation.selectNavItem(areasNav);

    // List all species once page has loaded
    bjørneparkappen.adminconsole.api.listAreas();

    // Display List Areas page
    bjørneparkappen.adminconsole.navigation.displayPage(listAreasPage);
};


// List Areas Page and elements
var listAreasPage = document.getElementById('list_areas_page');
var createAreaButton = document.getElementById('list_areas_create');
createAreaButton.onclick = function(){

    // Clear page data
    bjørneparkappen.adminconsole.areas.clearPageData();

    // Display Create Area page
    bjørneparkappen.adminconsole.navigation.displayPage(areaDetailPage);
}

// Clears the Areas page
bjørneparkappen.adminconsole.areas.clearPageData = function(){

    // Clear search bar
    document.getElementById('list_areas_search').value = "";

    // Retrieve table to empty
    var table = document.getElementById('list_areas_table');

    // Empty table
    while(table.rows.length > 1) {

        table.deleteRow(1);
    }
};

// Adds a row to the Areas table
bjørneparkappen.adminconsole.areas.addToTable = function(id, label, type){

    var table = document.getElementById("list_areas_table");

    // Create new table row
    var row = table.insertRow();

    // Create cells to insert into the table
    var idCell = row.insertCell(0);
    var labelCell = row.insertCell(1);
    var typeCell = row.insertCell(2);
    var deleteCell = row.insertCell(3);

    // Provide values to the new row
    idCell.innerHTML = id;
    idCell.style.display = HIDDEN;
    labelCell.innerHTML = label;
    typeCell.innerHTML = type;
    deleteCell.innerHTML = "<a href='#'>Delete</a>"
};

// Filter Areas table
bjørneparkappen.adminconsole.areas.search = function(){

    var search = document.getElementById("list_areas_search");
    var table = document.getElementById("list_areas_table");

    bjørneparkappen.adminconsole.lookup(search, table);
}


// Area Detail Page and elements
var areaDetailPage = document.getElementById('area_detail_page');
var cancelAreaButton = document.getElementById('area_detail_cancel');
cancelAreaButton.onclick = function(){

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



/** Animals */

// Navigation Item
var animalsNav = document.getElementById('animals');
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


// List Animals Page and elements
var listAnimalsPage = document.getElementById('list_animals_page');
var createAnimalButton = document.getElementById('list_animals_create');
createAnimalButton.onclick = function(){

    // Clear page data
    bjørneparkappen.adminconsole.animals.clearPageData();

    // Display Create Animal page
    bjørneparkappen.adminconsole.navigation.displayPage(animalDetailPage);
}

// Clears the Animals page
bjørneparkappen.adminconsole.animals.clearPageData = function(){

    // Clear search bar
    document.getElementById('list_animals_search').value = "";

    // Retrieve table to empty
    var table = document.getElementById('list_animals_table');

    // Empty table
    while(table.rows.length > 1) {

        table.deleteRow(1);
    }
};

// Adds a row to the Animals table
bjørneparkappen.adminconsole.animals.addToTable = function(id, name, species, description, available){

    var table = document.getElementById("list_animals_table");

    // Create new table row
    var row = table.insertRow();

    // Create cells to insert into the table
    var idCell = row.insertCell(0);
    var nameCell = row.insertCell(1);
    var speciesCell = row.insertCell(2);
    var descriptionCell = row.insertCell(3);
    var availableCell = row.insertCell(4);
    var deleteCell = row.insertCell(5);

    // Provide values to the new row
    idCell.innerHTML = id;
    idCell.style.display = HIDDEN;
    nameCell.innerHTML = name;
    speciesCell.innerHTML = species.common_name;
    descriptionCell.innerHTML = description;
    descriptionCell.style.display = HIDDEN;

    var checkbox = document.createElement('input');
    checkbox.type = "checkbox";
    checkbox.checked = available;
    availableCell.appendChild(checkbox);
    deleteCell.innerHTML = "<a href='#'>Delete</a>"
};

// Filter Animals table
bjørneparkappen.adminconsole.animals.search = function(){

    var search = document.getElementById("list_animals_search");
    var table = document.getElementById("list_animals_table");

    bjørneparkappen.adminconsole.lookup(search, table);
}


// Animal Detail Page and elements
var animalDetailPage = document.getElementById('animal_detail_page');
var cancelAnimalButton = document.getElementById('animal_detail_cancel');
cancelAnimalButton.onclick = function(){

     // Display List Animals page
     bjørneparkappen.adminconsole.navigation.displayPage(listAnimalsPage);
}



/** Events */

// Navigation Item
var eventsNav = document.getElementById('events');
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


// List Events Page and elements
var listEventsPage = document.getElementById('list_events_page');
var createEventButton = document.getElementById('list_events_create');
createEventButton.onclick = function(){

    // Clear page data
    bjørneparkappen.adminconsole.events.clearPageData();

    // Display Create Event page
    bjørneparkappen.adminconsole.navigation.displayPage(eventDetailPage);
}

// Clears the Events page
bjørneparkappen.adminconsole.events.clearPageData = function(){

    // Clear search bar
    document.getElementById('list_events_search').value = "";

    // Retrieve table to empty
    var table = document.getElementById('list_events_table');

    // Empty table
    while(table.rows.length > 1) {

        table.deleteRow(1);
    }
};

// Adds a row to the Events table
bjørneparkappen.adminconsole.events.addToTable = function(id, label, startTime, endTime, description, keeper, active){

    var table = document.getElementById("list_events_table");

    // Create new table row
    var row = table.insertRow();

    // Create cells to insert into the table
    var idCell = row.insertCell(0);
    var labelCell = row.insertCell(1);
    var startTimeCell = row.insertCell(2);
    var endTimeCell = row.insertCell(3);
    var descriptionCell = row.insertCell(4);
    var keeperCell = row.insertCell(5);
    var activeCell = row.insertCell(6);
    var deleteCell = row.insertCell(7);

    // Provide values to the new row
    idCell.innerHTML = id;
    idCell.style.display = HIDDEN;
    labelCell.innerHTML = label;
    startTimeCell.innerHTML = startTime;
    endTimeCell.innerHTML = endTime;
    descriptionCell.innerHTML = description;
    descriptionCell.style.display = HIDDEN;
    if (keeper != null){

        keeperCell.innerHTML = keeper.name;

    } else {

        keeperCell.innerHTML = "-";
    }
    var checkbox = document.createElement('input');
    checkbox.type = "checkbox";
    checkbox.checked = active;
    activeCell.appendChild(checkbox);
    deleteCell.innerHTML = "<a href='#'>Delete</a>"
};

// Filter Events table
bjørneparkappen.adminconsole.events.search = function(){

    var search = document.getElementById("list_events_search");
    var table = document.getElementById("list_events_table");

    bjørneparkappen.adminconsole.lookup(search, table);
}


// Event Detail Page and elements
var eventDetailPage = document.getElementById('event_detail_page');
var cancelEventButton = document.getElementById('event_detail_cancel');
cancelEventButton.onclick = function(){
    // Display List Events page
    bjørneparkappen.adminconsole.navigation.displayPage(listEventsPage);
}



/** Keepers */

// Navigation Item
var keepersNav = document.getElementById('keepers');
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

// List Keepers Page and elements
var listKeepersPage = document.getElementById('list_keepers_page');
var createKeeperButton = document.getElementById('list_keepers_create');
createKeeperButton.onclick = function(){

    // Clear page data
    bjørneparkappen.adminconsole.keepers.clearPageData();

    // Display Create Keeper page
    bjørneparkappen.adminconsole.navigation.displayPage(keeperDetailPage);
}

// Clears the Keepers page
bjørneparkappen.adminconsole.keepers.clearPageData = function(){

    // Clear search bar
    document.getElementById('list_keepers_search').value = "";

    // Retrieve table to empty
    var table = document.getElementById('list_keepers_table');

    // Empty table
    while(table.rows.length > 1) {

        table.deleteRow(1);
    }
};

// Adds a row to the Keepers table
bjørneparkappen.adminconsole.keepers.addToTable = function(id, name, bio){

    var table = document.getElementById("list_keepers_table");

    // Create new table row
    var row = table.insertRow();

    // Create cells to insert into the table
    var idCell = row.insertCell(0);
    var nameCell = row.insertCell(1);
    var bioCell = row.insertCell(2);
    var deleteCell = row.insertCell(3);

    // Provide values to the new row
    idCell.innerHTML = id;
    idCell.style.display = HIDDEN;
    nameCell.innerHTML = name;
    bioCell.innerHTML = bio;
    deleteCell.innerHTML = "<a href='#'>Delete</a>"
};

// Filter Keepers table
bjørneparkappen.adminconsole.keepers.search = function(){

    var search = document.getElementById("list_keepers_search");
    var table = document.getElementById("list_keepers_table");

    bjørneparkappen.adminconsole.lookup(search, table);
}


// Keeper Detail Page and elements
var keeperDetailPage = document.getElementById('keeper_detail_page');
var cancelKeeperButton = document.getElementById('keeper_detail_cancel');
cancelKeeperButton.onclick = function(){

    // Display List Keepers page
    bjørneparkappen.adminconsole.navigation.displayPage(listKeepersPage);
}



/** API Calls */
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
bjørneparkappen.adminconsole.api.listAreas = function(){
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
                resp.amenities = resp.amenities || [];
                resp.enclosures = resp.enclosures || [];

                // If amenities returned
                if (resp.amenities.length > 0) {

                    // Iterate through each one and display it
                    for (var i = 0; i < resp.amenities.length; i++) {

                        var id = resp.amenities[i].id;
                        var label = resp.amenities[i].label;
                        var type = resp.amenities[i].amenity_type;

                        // Add event to table
                        bjørneparkappen.adminconsole.areas.addToTable(id, label, type);
                    }
                }

                // If enclosures returned
                if (resp.enclosures.length > 0) {

                    // Iterate through each one and display it
                    for (var i = 0; i < resp.enclosures.length; i++) {

                        var id = resp.enclosures[i].id;
                        var label = resp.enclosures[i].label;
                        var type = "ENCLOSURE";

                        // Add event to table
                        bjørneparkappen.adminconsole.areas.addToTable(id, label, type);
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
bjørneparkappen.adminconsole.api.listEvents = function(){
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
                resp.events = resp.events || [];
                resp.feedings = resp.feedings || [];

                // If events returned
                if (resp.events.length > 0) {

                    // Iterate through each one and display it
                    for (var i = 0; i < resp.events.length; i++) {

                        var id = resp.events[i].id;
                        var label = resp.events[i].label;
                        var startTime = resp.events[i].start_time;
                        var endTime = resp.events[i].end_time;
                        var description = resp.events[i].description;
                        var available = resp.events[i].is_available;

                        // Add event to table
                        bjørneparkappen.adminconsole.events.addToTable(id, label, startTime, endTime, description, null, available);
                    }
                }

                // If feedings returned
                if (resp.feedings.length > 0) {

                    // Iterate through each one and display it
                    for (var i = 0; i < resp.feedings.length; i++) {

                        var id = resp.feedings[i].id;
                        var label = resp.feedings[i].label;
                        var startTime = resp.feedings[i].start_time;
                        var endTime = resp.feedings[i].end_time;
                        var description = resp.feedings[i].description;
                        var keeper = resp.feedings[i].keeper;
                        var available = resp.feedings[i].is_available;

                        // Add event to table
                        bjørneparkappen.adminconsole.events.addToTable(id, label, startTime, endTime, description, keeper, available);
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
bjørneparkappen.adminconsole.api.listAnimals = function(){

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
                resp.animals = resp.animals || [];

                // If animals returned
                if (resp.animals.length > 0) {

                    // Iterate through each one and display it
                    for (var i = 0; i < resp.animals.length; i++) {

                        var id = resp.animals[i].id;
                        var name = resp.animals[i].name;
                        var species = resp.animals[i].species;
                        var description = resp.animals[i].description;
                        var available = resp.animals[i].is_available;

                        // Add animal to table
                        bjørneparkappen.adminconsole.animals.addToTable(id, name, species, description, available);
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
bjørneparkappen.adminconsole.api.listKeepers = function(){

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
                resp.keepers = resp.keepers || [];

                // If keepers returned
                if (resp.keepers.length > 0) {

                    // Iterate through each one and display it
                    for (var i = 0; i < resp.keepers.length; i++) {

                        var id = resp.keepers[i].id;
                        var name = resp.keepers[i].name;
                        var bio = resp.keepers[i].bio;

                        // Add species to table
                        bjørneparkappen.adminconsole.keepers.addToTable(id, name, bio);
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
