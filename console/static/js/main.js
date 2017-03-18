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

    // Scroll to top of page
    window.scrollTo(0, 0);

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

bjørneparkappen.adminconsole.addTranslation = function(item, itemProperty, textInput, languageInput, translationsSpan, addButton){

    if (textInput.value != "" && languageInput.value != ""){

        // Create visual representation of translation
        var translationParagraph = document.createElement('p');
        var selectedLanguage = languageInput.options[languageInput.selectedIndex];
        var translationText = textInput.value;
        var translationLanguage = selectedLanguage.value;
        translationParagraph.innerHTML = "<label for='translation_" + translationLanguage + "'></label><i class='translation' id='translation_" + translationLanguage + "'>" + translationText + " (" + selectedLanguage.text + ")" + "</i>";

        // Add to translation tag
        translationsSpan.appendChild(translationParagraph);

        // Clear textbox
        textInput.value = "";

        // Remove language option
        languageInput.remove(translationLanguage);

        // Add item to array
        var translation = {};
        translation.text = translationText;
        translation.language_code = translationLanguage;

        if (item[itemProperty] == null){

            item[itemProperty] = [];
        }
        item[itemProperty].push(translation);

        // Disable input box if no more translations available to add
        if (languageInput.length == 0) {

            textInput.disabled = true;
            languageInput.disabled = true;
            addButton.disabled = true;
        }
    }
}

bjørneparkappen.adminconsole.getTranslation = function(item, itemProperty, languageCode) {

    for (i = 0; i < item[itemProperty].length; i++){

        if (item[itemProperty][i].language_code == languageCode){

            return item[itemProperty][i].text;
        }
    }
}

bjørneparkappen.adminconsole.updateTranslation = function(item, itemProperty, newValue, languageCode){

    if (newValue != "" && languageCode != ""){

        for (i = 0; i < item[itemProperty].length; i++){

            if (item[itemProperty][i].language_code == languageCode) {

                item[itemProperty][i].text = newValue;
                alert("Updated!");
                break;
            }
        }
    }
}

bjørneparkappen.adminconsole.clearTranslations = function(textInput, languageInput, translationsSpan, addButton){

    translationsSpan.innerHTML = "";
    textInput.disabled = false;

    var english = document.createElement('option');
    english.value = "en";
    english.text = "English";
    var norwegian = document.createElement('option');
    norwegian.value = "no";
    norwegian.text = "Norwegian";
    languageInput.innerHTML = "";
    languageInput.appendChild(english);
    languageInput.appendChild(norwegian);
    languageInput.disabled = false;

    addButton.disabled = false;
}



/** API Calls */
bjørneparkappen.adminconsole.api.getSpecies = function(){

    // Put UI into wait state
    bjørneparkappen.adminconsole.startWait();

    // Create new request
    var xhr = new XMLHttpRequest();

    // Open new GET request
    xhr.open('GET', BASE_URL + "species/all_languages?key=" + API_KEY);

    // GET request state change callback event
    xhr.onreadystatechange = function() {

        // If request has completed
        if (xhr.readyState == XMLHttpRequest.DONE){

            // If request status is 200
            if (xhr.status == 200){

                // Parse response JSON
                resp = JSON.parse(xhr.responseText);
                speciesList = resp.species || [];
            }

            // Terminate UI wait state
            bjørneparkappen.adminconsole.endWait();
        }
    };

    // Send request
    xhr.send();
};
bjørneparkappen.adminconsole.api.createSpecies = function(species){

    // Put UI into wait state
    bjørneparkappen.adminconsole.startWait();

    // Create new request
    var xhr = new XMLHttpRequest();

    // Open new POST request
    xhr.open('POST', BASE_URL + "species/create?key=" + API_KEY);

    // POST request state change callback event
    xhr.onreadystatechange = function() {

        // If request has completed
        if (xhr.readyState == XMLHttpRequest.DONE){

            // If request status is 200
            if (xhr.status == 200) {

                // Parse response JSON
                resp = JSON.parse(xhr.responseText);
                speciesList = resp.species || [];

                // Load the List Species page with new data
                bjørneparkappen.adminconsole.species.list.loadPage();

                // Display the List Species page
                bjørneparkappen.adminconsole.navigation.displayPage(listSpeciesPage);

            } else {

                // Display error message
                resp = JSON.parse(xhr.responseText);
                alert(resp.error.message);
            }

            // Terminate UI wait state
            bjørneparkappen.adminconsole.endWait();
        }
    };

    // Set content type
    xhr.setRequestHeader("Content-type", "application/json");

    // Send request
    xhr.send(JSON.stringify(species));
}
bjørneparkappen.adminconsole.api.updateSpecies = function(species){

    // Put UI into wait state
    bjørneparkappen.adminconsole.startWait();

    // Create new request
    var xhr = new XMLHttpRequest();

    // Open new POST request
    xhr.open('POST', BASE_URL + "species/update?id=" + species.id + "&key=" + API_KEY);

    // POST request state change callback event
    xhr.onreadystatechange = function() {

        // If request has completed
        if (xhr.readyState == XMLHttpRequest.DONE){

            // If request status is 200
            if (xhr.status == 200) {

                // Parse response JSON
                resp = JSON.parse(xhr.responseText);
                speciesList = resp.species || [];

                // Load the List Species page with new data
                bjørneparkappen.adminconsole.species.list.loadPage();

                // Display the List Species page
                bjørneparkappen.adminconsole.navigation.displayPage(listSpeciesPage);

            } else {

                // Display error message
                resp = JSON.parse(xhr.responseText);
                alert(resp.error.message);
            }

            // Terminate UI wait state
            bjørneparkappen.adminconsole.endWait();
        }
    };

    // Set content type
    xhr.setRequestHeader("Content-type", "application/json");

    // Send request
    xhr.send(JSON.stringify(species));
}
bjørneparkappen.adminconsole.api.deleteSpecies = function(species){

    // Put UI into wait state
    bjørneparkappen.adminconsole.startWait();

    // Create new request
    var xhr = new XMLHttpRequest();

    // Open new DELETE request
    xhr.open('DELETE', BASE_URL + "species/delete?id=" + species.id + "&key=" + API_KEY);

    // DELETE request state change callback event
    xhr.onreadystatechange = function() {

        // If request has completed
        if (xhr.readyState == XMLHttpRequest.DONE){

            // If request status is 200
            if (xhr.status == 200){

                // Parse response JSON
                resp = JSON.parse(xhr.responseText);
                speciesList = resp.species || [];

                // Load the List Species page with new data
                bjørneparkappen.adminconsole.species.list.loadPage();

                // Display the List Species page
                bjørneparkappen.adminconsole.navigation.displayPage(listSpeciesPage);

            } else {

                // Display error message
                resp = JSON.parse(xhr.responseText);
                alert(resp.error.message);
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
    xhr.open('GET', BASE_URL + "areas/all_languages?key=" + API_KEY);

    // GET request state change callback event
    xhr.onreadystatechange = function() {

        // If request has completed
        if (xhr.readyState == XMLHttpRequest.DONE){

            // If request status is 200
            if (xhr.status == 200){

                // Parse response JSON
                resp = JSON.parse(xhr.responseText);
                amenitiesList = resp.amenities || [];
                enclosuresList = resp.enclosures || [];
            }

            // Terminate UI wait state
            bjørneparkappen.adminconsole.endWait();
        }
    };

    // Send request
    xhr.send();
};
bjørneparkappen.adminconsole.api.createAmenity = function(amenity){

    // Put UI into wait state
    bjørneparkappen.adminconsole.startWait();

    // Create new request
    var xhr = new XMLHttpRequest();

    // Open new POST request
    xhr.open('POST', BASE_URL + "areas/amenities/create?key=" + API_KEY);

    // POST request state change callback event
    xhr.onreadystatechange = function() {

        // If request has completed
        if (xhr.readyState == XMLHttpRequest.DONE){

            // If request status is 200
            if (xhr.status == 200) {

                // Parse response JSON
                resp = JSON.parse(xhr.responseText);
                amenitiesList = resp.amenities || [];

                // Load the List Areas page with new data
                bjørneparkappen.adminconsole.areas.list.loadPage();

                // Display the List Areas page
                bjørneparkappen.adminconsole.navigation.displayPage(listAreasPage);

            } else {

                // Display error message
                resp = JSON.parse(xhr.responseText);
                alert(resp.error.message);
            }

            // Terminate UI wait state
            bjørneparkappen.adminconsole.endWait();
        }
    };

    // Set content type
    xhr.setRequestHeader("Content-type", "application/json");

    // Send request
    xhr.send(JSON.stringify(amenity));
}
bjørneparkappen.adminconsole.api.updateAmenity = function(amenity){

    // Put UI into wait state
    bjørneparkappen.adminconsole.startWait();

    // Create new request
    var xhr = new XMLHttpRequest();

    // Open new POST request
    xhr.open('POST', BASE_URL + "areas/amenities/update?id=" + amenity.id + "&key=" + API_KEY);

    // POST request state change callback event
    xhr.onreadystatechange = function() {

        // If request has completed
        if (xhr.readyState == XMLHttpRequest.DONE){

            // If request status is 200
            if (xhr.status == 200) {

                // Parse response JSON
                resp = JSON.parse(xhr.responseText);
                amenitiesList = resp.amenities || [];

                // Load the List Areas page with new data
                bjørneparkappen.adminconsole.areas.list.loadPage();

                // Display the List Areas page
                bjørneparkappen.adminconsole.navigation.displayPage(listAreasPage);

            } else {

                // Display error message
                resp = JSON.parse(xhr.responseText);
                alert(resp.error.message);
            }

            // Terminate UI wait state
            bjørneparkappen.adminconsole.endWait();
        }
    };

    // Set content type
    xhr.setRequestHeader("Content-type", "application/json");

    // Send request
    xhr.send(JSON.stringify(amenity));
}
bjørneparkappen.adminconsole.api.createEnclosure = function(enclosure){

    // Put UI into wait state
    bjørneparkappen.adminconsole.startWait();

    // Create new request
    var xhr = new XMLHttpRequest();

    // Open new POST request
    xhr.open('POST', BASE_URL + "areas/enclosures/create?key=" + API_KEY);

    // POST request state change callback event
    xhr.onreadystatechange = function() {

        // If request has completed
        if (xhr.readyState == XMLHttpRequest.DONE){

            // If request status is 200
            if (xhr.status == 200) {

                // Parse response JSON
                resp = JSON.parse(xhr.responseText);
                enclosuresList = resp.enclosures || [];

                // Load the List Areas page with new data
                bjørneparkappen.adminconsole.areas.list.loadPage();

                // Display the List Areas page
                bjørneparkappen.adminconsole.navigation.displayPage(listAreasPage);

            } else {

                // Display error message
                resp = JSON.parse(xhr.responseText);
                alert(resp.error.message);
            }

            // Terminate UI wait state
            bjørneparkappen.adminconsole.endWait();
        }
    };

    // Set content type
    xhr.setRequestHeader("Content-type", "application/json");

    // Send request
    xhr.send(JSON.stringify(enclosure));
}
bjørneparkappen.adminconsole.api.updateEnclosure = function(enclosure){

    // Put UI into wait state
    bjørneparkappen.adminconsole.startWait();

    // Create new request
    var xhr = new XMLHttpRequest();

    // Open new POST request
    xhr.open('POST', BASE_URL + "areas/enclosures/update?id=" + enclosure.id + "&key=" + API_KEY);

    // POST request state change callback event
    xhr.onreadystatechange = function() {

        // If request has completed
        if (xhr.readyState == XMLHttpRequest.DONE){

            // If request status is 200
            if (xhr.status == 200) {

                // Parse response JSON
                resp = JSON.parse(xhr.responseText);
                enclosuresList = resp.enclosures || [];

                // Load the List Areas page with new data
                bjørneparkappen.adminconsole.areas.list.loadPage();

                // Display the List Areas page
                bjørneparkappen.adminconsole.navigation.displayPage(listAreasPage);

            } else {

                // Display error message
                resp = JSON.parse(xhr.responseText);
                alert(resp.error.message);
            }

            // Terminate UI wait state
            bjørneparkappen.adminconsole.endWait();
        }
    };

    // Set content type
    xhr.setRequestHeader("Content-type", "application/json");

    // Send request
    xhr.send(JSON.stringify(enclosure));
}
bjørneparkappen.adminconsole.api.deleteArea = function(area){

    // Put UI into wait state
    bjørneparkappen.adminconsole.startWait();

    // Create new request
    var xhr = new XMLHttpRequest();

    // Open new DELETE request
    xhr.open('DELETE', BASE_URL + "areas/delete?id=" + area.id + "&key=" + API_KEY);

    // DELETE request state change callback event
    xhr.onreadystatechange = function() {

        // If request has completed
        if (xhr.readyState == XMLHttpRequest.DONE){

            // If request status is 200
            if (xhr.status == 200){

                // Parse response JSON
                resp = JSON.parse(xhr.responseText);
                amenitiesList = resp.amenities || [];
                enclosuresList = resp.enclosures || [];

                // Load the List Areas page with new data
                bjørneparkappen.adminconsole.areas.list.loadPage();

                // Display the List Areas page
                bjørneparkappen.adminconsole.navigation.displayPage(listAreasPage);

            } else {

                // Display error message
                resp = JSON.parse(xhr.responseText);
                alert(resp.error.message);
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
    xhr.open('GET', BASE_URL + "animals/all_languages?key=" + API_KEY);

    // GET request state change callback event
    xhr.onreadystatechange = function() {

        // If request has completed
        if (xhr.readyState == XMLHttpRequest.DONE){

            // If request status is 200
            if (xhr.status == 200){

                // Parse response JSON
                resp = JSON.parse(xhr.responseText);
                animalsList = resp.animals || [];
            }

            // Terminate UI wait state
            bjørneparkappen.adminconsole.endWait();
        }
    };

    // Send request
    xhr.send();
};
bjørneparkappen.adminconsole.api.createAnimal = function(animal){

    // Put UI into wait state
    bjørneparkappen.adminconsole.startWait();

    // Create new request
    var xhr = new XMLHttpRequest();

    // Open new POST request
    xhr.open('POST', BASE_URL + "animals/create?key=" + API_KEY);

    // POST request state change callback event
    xhr.onreadystatechange = function() {

        // If request has completed
        if (xhr.readyState == XMLHttpRequest.DONE){

            // If request status is 200
            if (xhr.status == 200) {

                // Parse response JSON
                resp = JSON.parse(xhr.responseText);
                animalsList = resp.animals || [];

                // Load the List Animals page with new data
                bjørneparkappen.adminconsole.animals.list.loadPage();

                // Display the List Animals page
                bjørneparkappen.adminconsole.navigation.displayPage(listAnimalsPage);

            } else {

                // Display error message
                resp = JSON.parse(xhr.responseText);
                alert(resp.error.message);
            }

            // Terminate UI wait state
            bjørneparkappen.adminconsole.endWait();
        }
    };

    // Set content type
    xhr.setRequestHeader("Content-type", "application/json");

    // Send request
    xhr.send(JSON.stringify(animal));
}
bjørneparkappen.adminconsole.api.updateAnimal = function(animal){

    // Put UI into wait state
    bjørneparkappen.adminconsole.startWait();

    // Create new request
    var xhr = new XMLHttpRequest();

    // Open new POST request
    xhr.open('POST', BASE_URL + "animals/update?animal_id=" + animal.id + "&species_id=" + animal.species.id + "&enclosure_id=" + animal.enclosure_id + "&key=" + API_KEY);

    // POST request state change callback event
    xhr.onreadystatechange = function() {

        // If request has completed
        if (xhr.readyState == XMLHttpRequest.DONE){

            // If request status is 200
            if (xhr.status == 200) {

                // Parse response JSON
                resp = JSON.parse(xhr.responseText);
                animalsList = resp.animals || [];

                // Load the List Animals page with new data
                bjørneparkappen.adminconsole.animals.list.loadPage();

                // Display the List Animals page
                bjørneparkappen.adminconsole.navigation.displayPage(listAnimalsPage);

            } else {

                // Display error message
                resp = JSON.parse(xhr.responseText);
                alert(resp.error.message);
            }

            // Terminate UI wait state
            bjørneparkappen.adminconsole.endWait();
        }
    };

    // Set content type
    xhr.setRequestHeader("Content-type", "application/json");

    // Send request
    xhr.send(JSON.stringify(animal));
}
bjørneparkappen.adminconsole.api.deleteAnimal = function(animal){

    // Put UI into wait state
    bjørneparkappen.adminconsole.startWait();

    // Create new request
    var xhr = new XMLHttpRequest();

    // Open new DELETE request
    xhr.open('DELETE', BASE_URL + "animals/delete?animal_id=" + animal.id + "&species_id=" + animal.species.id + "key=" + API_KEY);

    // DELETE request state change callback event
    xhr.onreadystatechange = function() {

        // If request has completed
        if (xhr.readyState == XMLHttpRequest.DONE){

            // If request status is 200
            if (xhr.status == 200){

                // Parse response JSON
                resp = JSON.parse(xhr.responseText);
                animalsList = resp.animals || [];

                // Load the List Animals page with new data
                bjørneparkappen.adminconsole.animals.list.loadPage();

                // Display the List Animals page
                bjørneparkappen.adminconsole.navigation.displayPage(listAnimalsPage);

            } else {

                // Display error message
                resp = JSON.parse(xhr.responseText);
                alert(resp.error.message);
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
    xhr.open('GET', BASE_URL + "keepers/all_languages?key=" + API_KEY);

    // GET request state change callback event
    xhr.onreadystatechange = function() {

        // If request has completed
        if (xhr.readyState == XMLHttpRequest.DONE){

            // If request status is 200
            if (xhr.status == 200){

                // Parse response JSON
                resp = JSON.parse(xhr.responseText);
                keepersList = resp.keepers || [];
            }

            // Terminate UI wait state
            bjørneparkappen.adminconsole.endWait();
        }
    };

    // Send request
    xhr.send();
};
bjørneparkappen.adminconsole.api.createKeeper = function(keeper){

    // Put UI into wait state
    bjørneparkappen.adminconsole.startWait();

    // Create new request
    var xhr = new XMLHttpRequest();

    // Open new POST request
    xhr.open('POST', BASE_URL + "keepers/create?key=" + API_KEY);

    // POST request state change callback event
    xhr.onreadystatechange = function() {

        // If request has completed
        if (xhr.readyState == XMLHttpRequest.DONE){

            // If request status is 200
            if (xhr.status == 200) {

                // Parse response JSON
                resp = JSON.parse(xhr.responseText);
                keepersList = resp.keepers || [];

                // Load the List Keepers page with new data
                bjørneparkappen.adminconsole.keepers.list.loadPage();

                // Display the List Keepers page
                bjørneparkappen.adminconsole.navigation.displayPage(listKeepersPage);

            } else {

                // Display error message
                resp = JSON.parse(xhr.responseText);
                alert(resp.error.message);
            }

            // Terminate UI wait state
            bjørneparkappen.adminconsole.endWait();
        }
    };

    // Set content type
    xhr.setRequestHeader("Content-type", "application/json");

    // Send request
    xhr.send(JSON.stringify(keeper));
}
bjørneparkappen.adminconsole.api.updateKeeper = function(keeper){

    // Put UI into wait state
    bjørneparkappen.adminconsole.startWait();

    // Create new request
    var xhr = new XMLHttpRequest();

    // Open new POST request
    xhr.open('POST', BASE_URL + "keepers/update?id=" + keeper.id + "&key=" + API_KEY);

    // POST request state change callback event
    xhr.onreadystatechange = function() {

        // If request has completed
        if (xhr.readyState == XMLHttpRequest.DONE){

            // If request status is 200
            if (xhr.status == 200) {

                // Parse response JSON
                resp = JSON.parse(xhr.responseText);
                keepersList = resp.keepers || [];

                // Load the List Keeper page with new data
                bjørneparkappen.adminconsole.keepers.list.loadPage();

                // Display the List Keeper page
                bjørneparkappen.adminconsole.navigation.displayPage(listKeepersPage);

            } else {

                // Display error message
                resp = JSON.parse(xhr.responseText);
                alert(resp.error.message);
            }

            // Terminate UI wait state
            bjørneparkappen.adminconsole.endWait();
        }
    };

    // Set content type
    xhr.setRequestHeader("Content-type", "application/json");

    // Send request
    xhr.send(JSON.stringify(keeper));
}
bjørneparkappen.adminconsole.api.deleteKeeper = function(keeper){

    // Put UI into wait state
    bjørneparkappen.adminconsole.startWait();

    // Create new request
    var xhr = new XMLHttpRequest();

    // Open new DELETE request
    xhr.open('DELETE', BASE_URL + "keepers/delete?id=" + keeper.id + "&key=" + API_KEY);

    // DELETE request state change callback event
    xhr.onreadystatechange = function() {

        // If request has completed
        if (xhr.readyState == XMLHttpRequest.DONE){

            // If request status is 200
            if (xhr.status == 200){

                // Parse response JSON
                resp = JSON.parse(xhr.responseText);
                keepersList = resp.keepers || [];

                // Load the List Keepers page with new data
                bjørneparkappen.adminconsole.keepers.list.loadPage();

                // Display the List Keepers page
                bjørneparkappen.adminconsole.navigation.displayPage(listKeepersPage);

            } else {

                // Display error message
                resp = JSON.parse(xhr.responseText);
                alert(resp.error.message);
            }

            // Terminate UI wait state
            bjørneparkappen.adminconsole.endWait();

        }
    };

    // Send request
    xhr.send();
};



/** Species */
var speciesList = [];

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
    if (speciesList.length == 0){

        // Retrieve the list of species
        bjørneparkappen.adminconsole.api.getSpecies();
    }

    // Iterate the species list and add each to the table
    for (var i = 0; i < speciesList.length; i++) {

        bjørneparkappen.adminconsole.species.addToTable(speciesList[i]);
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
    commonNameCell.innerHTML = bjørneparkappen.adminconsole.getTranslation(species, 'common_name', LANGUAGE_CODE);
    latinCell.innerHTML = species.latin;

    // Create link to delete item
    var deleteLink = document.createElement('a');
    deleteLink.href = '#';
    deleteLink.innerHTML = "Delete"
    deleteLink.onclick = function(){

        bjørneparkappen.adminconsole.species.delete(species);
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

    if (confirm("Are you sure?")){

        bjørneparkappen.adminconsole.api.deleteSpecies(species);
    }
}


// Species Detail Page and elements
var speciesDetailSpecies = {};
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
var speciesCommonNameLanguageInput = document.getElementById('species_common_name_language');
speciesCommonNameLanguageInput.onchange = function(){

    if (speciesDetailSpecies.id != null){

        // Update textbox value
        speciesCommonNameInput.value = bjørneparkappen.adminconsole.getTranslation(speciesDetailSpecies, 'common_name', speciesCommonNameLanguageInput.value);
    }
}
var speciesCommonNameTranslations = document.getElementById('species_common_name_translations');
var speciesCommonNameAddTranslation = document.getElementById('species_add_common_name_translation');
speciesCommonNameAddTranslation.onclick = function(){

    // If creating
    if (speciesDetailSpecies.id == null){

        // Add translation to list of displayed translations
        bjørneparkappen.adminconsole.addTranslation(speciesDetailSpecies, 'common_name', speciesCommonNameInput, speciesCommonNameLanguageInput, speciesCommonNameTranslations, speciesCommonNameAddTranslation);

    } else {    // Else if updating

        // Update existing translation
        bjørneparkappen.adminconsole.updateTranslation(speciesDetailSpecies, 'common_name', speciesCommonNameInput.value, speciesCommonNameLanguageInput.value);
    }
}
var speciesDescriptionInput = document.getElementById('species_description');
var speciesDescriptionLanguageInput = document.getElementById('species_description_language');
speciesDescriptionLanguageInput.onchange = function(){

    if (speciesDetailSpecies.id != null){

        // Update textbox value
        speciesDescriptionInput.value = bjørneparkappen.adminconsole.getTranslation(speciesDetailSpecies, 'description', speciesDescriptionLanguageInput.value);
    }
}
var speciesDescriptionTranslations = document.getElementById('species_description_translations');
var speciesDescriptionAddTranslation = document.getElementById('species_add_description_translation');
speciesDescriptionAddTranslation.onclick = function(){

    // If creating
    if (speciesDetailSpecies.id == null){

        // Add translation to list of displayed translations
        bjørneparkappen.adminconsole.addTranslation(speciesDetailSpecies, 'description', speciesDescriptionInput, speciesDescriptionLanguageInput, speciesDescriptionTranslations, speciesDescriptionAddTranslation);

    } else {    // Else if updating

        // Update existing translation
        bjørneparkappen.adminconsole.updateTranslation(speciesDetailSpecies, 'description', speciesDescriptionInput.value, speciesDescriptionLanguageInput.value);
    }
}
var speciesLatinInput = document.getElementById('species_latin');
var speciesDescriptionInput = document.getElementById('species_description');

// Clears the Species Detail page
bjørneparkappen.adminconsole.species.detail.clearPageData = function(){

    // Reset species object
    speciesDetailSpecies = {};

    // Clear translation inputs and re-enable associated controls
    bjørneparkappen.adminconsole.clearTranslations(speciesCommonNameInput, speciesCommonNameLanguageInput, speciesCommonNameTranslations, speciesCommonNameAddTranslation);

    // Clear translation inputs and re-enable associated controls
    bjørneparkappen.adminconsole.clearTranslations(speciesDescriptionInput, speciesDescriptionLanguageInput, speciesDescriptionTranslations, speciesDescriptionAddTranslation);

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
    speciesCommonNameAddTranslation.innerHTML = "Add";
    speciesDescriptionAddTranslation.innerHTML = "Add";

    speciesDetailConfirm.onclick = function(){

        speciesDetailSpecies.latin = speciesLatinInput.value;

        if (bjørneparkappen.adminconsole.species.detail.validate()){

            bjørneparkappen.adminconsole.api.createSpecies(speciesDetailSpecies);
        }
    }

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
    speciesCommonNameAddTranslation.innerHTML = "Update";
    speciesDescriptionAddTranslation.innerHTML = "Update";

    // Set species object
    speciesDetailSpecies = species;

    speciesDetailConfirm.onclick = function(){

        speciesDetailSpecies.latin = speciesLatinInput.value;

        if (bjørneparkappen.adminconsole.species.detail.validate()){

            bjørneparkappen.adminconsole.api.updateSpecies(speciesDetailSpecies);
        }
    }

    // Populate form with new data
    speciesCommonNameInput.value = bjørneparkappen.adminconsole.getTranslation(speciesDetailSpecies, 'common_name', speciesCommonNameLanguageInput.value);
    speciesLatinInput.value = speciesDetailSpecies.latin;
    speciesDescriptionInput.value = bjørneparkappen.adminconsole.getTranslation(speciesDetailSpecies, 'description', speciesDescriptionLanguageInput.value);

    // Display the detail page
    bjørneparkappen.adminconsole.navigation.displayPage(speciesDetailPage);
}

// Validates detail input
bjørneparkappen.adminconsole.species.detail.validate = function(){

    var valid = false;

    if (speciesDetailSpecies.common_name.length < speciesCommonNameLanguageInput.length){

        alert("Please enter all translations for the species' common name.")

    } else if (speciesDetailSpecies.latin == ""){

        alert("Please enter a latin name.");

    } else if (speciesDetailSpecies.description.length < speciesDescriptionLanguageInput.length) {

        alert("Please enter all translations for the speces' description.");

    } else {

        valid = true;
    }

    return valid;
}



/** Areas */
var amenitiesList = [];
var enclosuresList = [];

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
    if (amenitiesList.length > 0 || enclosuresList.length > 0){

        // If amenities returned
        if (amenitiesList.length > 0) {

            // Iterate through each one and add it to the table
            for (var i = 0; i < amenitiesList.length; i++) {

                // Add event to table
                bjørneparkappen.adminconsole.areas.addToTable(amenitiesList[i]);
            }
        }

        // If enclosures returned
        if (enclosuresList.length > 0) {

            // Iterate through each one and add it to the table
            for (var i = 0; i < enclosuresList.length; i++) {

                // Add event to table
                bjørneparkappen.adminconsole.areas.addToTable(enclosuresList[i]);
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
    labelCell.innerHTML = bjørneparkappen.adminconsole.getTranslation(area, 'label', LANGUAGE_CODE);

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

    if (confirm("Are you sure?")){

        bjørneparkappen.adminconsole.api.deleteArea(area);
    }
}


// Area Detail Page and elements
var areaDetailArea = {};
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
var areaLabelLanguageInput = document.getElementById('area_label_language');
areaLabelLanguageInput.onchange = function(){

    if (areaDetailArea.id != null){

        // Update textbox value
        areaLabelInput.value = bjørneparkappen.adminconsole.getTranslation(areaDetailArea, 'label', areaLabelLanguageInput.value);
    }
}
var areaLabelTranslations = document.getElementById('area_label_translations');
var areaLabelAddTranslation = document.getElementById('area_add_label_translation');
areaLabelAddTranslation.onclick = function(){

    // If creating
    if (areaDetailArea.id == null){

        // Add translation to list of displayed translations
        bjørneparkappen.adminconsole.addTranslation(areaDetailArea, 'label', areaLabelInput, areaLabelLanguageInput, areaLabelTranslations, areaLabelAddTranslation);

    } else {    // Else if updating

        // Update existing translation
        bjørneparkappen.adminconsole.updateTranslation(areaDetailArea, 'label', areaLabelInput.value, areaLabelLanguageInput.value);
    }
}
var areaVisitorDestinationInput = document.getElementById('area_visitor_destination');
var amenityTypeInput = document.getElementById('amenity_type');
var amenityDescriptionInput = document.getElementById('amenity_description');
var amenityDescriptionLanguageInput = document.getElementById('amenity_description_language');
amenityDescriptionLanguageInput.onchange = function(){

    if (areaDetailArea.id != null){

        // Update textbox value
        amenityDescriptionInput.value = bjørneparkappen.adminconsole.getTranslation(areaDetailArea, 'description', amenityDescriptionLanguageInput.value);
    }
}
var amenityDescriptionTranslations = document.getElementById('amenity_description_translations');
var amenityDescriptionAddTranslation = document.getElementById('amenity_add_description_translation');
amenityDescriptionAddTranslation.onclick = function(){

    // If creating
    if (areaDetailArea.id == null){

        // Add translation to list of displayed translations
        bjørneparkappen.adminconsole.addTranslation(areaDetailArea, 'description', amenityDescriptionInput, amenityDescriptionLanguageInput, amenityDescriptionTranslations, amenityDescriptionAddTranslation);

    } else {    // Else if updating

        // Update existing translation
        bjørneparkappen.adminconsole.updateTranslation(areaDetailArea, 'description', amenityDescriptionInput.value, amenityDescriptionLanguageInput.value);
    }
}
var amenityImage = document.getElementById('amenity_image');

// Clears the Area Detail page
bjørneparkappen.adminconsole.areas.detail.clearPageData = function(){

    // Reset area object
    areaDetailArea = {};

    // Clear translation inputs and re-enable associated controls
    bjørneparkappen.adminconsole.clearTranslations(areaLabelInput, areaLabelLanguageInput, areaLabelTranslations, areaLabelAddTranslation);

    // Clear translation inputs and re-enable associated controls
    bjørneparkappen.adminconsole.clearTranslations(amenityDescriptionInput, amenityDescriptionLanguageInput, amenityDescriptionTranslations, amenityDescriptionAddTranslation);

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
    areaLabelAddTranslation.innerHTML = "Add";
    amenityDescriptionAddTranslation.innerHTML = "Add";

    areaDetailConfirm.onclick = function(){

        areaDetailArea.visitor_destination = areaVisitorDestinationInput.value;

        if (bjørneparkappen.adminconsole.areas.detail.validate()){

            if (areaTypeSelector.value == "amenity") {

                areaDetailArea.amenity_type = amenityTypeInput.value;
                bjørneparkappen.adminconsole.api.createAmenity(areaDetailArea);

            } else {

                bjørneparkappen.adminconsole.api.createEnclosure(areaDetailArea);
            }
        }
    }

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
    areaLabelAddTranslation.innerHTML = "Update";
    amenityDescriptionAddTranslation.innerHTML = "Update";

    // Set area object
    areaDetailArea = area;

    if (area.amenity_type != null){

        areaTypeSelector.value = "amenity";
        amenity_fields.style.display = DISPLAYED;

        amenityTypeInput.value = area.amenity_type;
        amenityDescriptionInput.value = bjørneparkappen.adminconsole.getTranslation(area, 'description', LANGUAGE_CODE);

    } else {

        areaTypeSelector.value = "enclosure";
        amenity_fields.style.display = HIDDEN;
    }

    areaDetailConfirm.onclick = function(){

        areaDetailArea.visitor_destination = areaVisitorDestinationInput.value;

        if (bjørneparkappen.adminconsole.areas.detail.validate()){

            if (areaTypeSelector.value == "amenity") {

                areaDetailArea.amenity_type = amenityTypeInput.value;
                bjørneparkappen.adminconsole.api.updateAmenity(areaDetailArea);

            } else {

                bjørneparkappen.adminconsole.api.updateEnclosure(areaDetailArea);
            }
        }
    }

    // Populate form with new data
    areaLabelInput.value = bjørneparkappen.adminconsole.getTranslation(area, 'label', LANGUAGE_CODE);
    areaVisitorDestinationInput.value = area.visitor_destination;

    // Display the detail page
    bjørneparkappen.adminconsole.navigation.displayPage(areaDetailPage);
}

// Validates detail input
bjørneparkappen.adminconsole.areas.detail.validate = function(){

    var valid = false;

    if (areaDetailArea.label.length < areaLabelLanguageInput.length){

        alert("Please enter all translations for the area label.")

    } else if (areaDetailArea.visitor_destination == ""){

        alert("Please enter a set of visitor destination co-ordinates.");

    } else if (areaTypeSelector.value == "amenity"){

        if (areaDetailArea.amenity_type == ""){

            alert("Please select an amenity type.");

        } else if (areaDetailArea.description.length < amenityDescriptionLanguageInput.length){

            alert("Please enter all translations for the amenity description");

        } else {

            valid = true;
        }

    } else {

        valid = true;
    }

    return valid;
}



/** Animals */
var animalsList = [];

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

    // If no animals returned
    if (animalsList.length == 0) {

        // Retrieve the list of animals
        bjørneparkappen.adminconsole.api.getAnimals();
    }

    // Iterate through each one and add each to the table
    for (var i = 0; i < animalsList.length; i++) {

        // Add animal to table
        bjørneparkappen.adminconsole.animals.addToTable(animalsList[i]);
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
    speciesCell.innerHTML = bjørneparkappen.adminconsole.getTranslation(animal.species, 'common_name', LANGUAGE_CODE);

    var checkbox = document.createElement('input');
    checkbox.type = "checkbox";
    checkbox.checked = animal.is_available;
    checkbox.onclick = function(){

        animal.is_available = !animal.is_available;
        bjørneparkappen.adminconsole.api.updateAnimal(animal);
    }
    availableCell.appendChild(checkbox);

    // Create link to delete item
    var deleteLink = document.createElement('a');
    deleteLink.href = '#';
    deleteLink.innerHTML = "Delete"
    deleteLink.onclick = function(){

            bjørneparkappen.adminconsole.animals.delete(animal);
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

    if (confirm("Are you sure?")){

        bjørneparkappen.adminconsole.api.deleteAnimal(animal);
    }
}


// Animal Detail Page and elements
var animalDetailAnimal = {};
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
var animalDescriptionLanguageInput = document.getElementById('animal_description_language');
animalDescriptionLanguageInput.onchange = function(){

    if (animalDetailAnimal.id != null){

        // Update textbox value
        animalDescriptionInput.value = bjørneparkappen.adminconsole.getTranslation(animalDetailAnimal, 'description', animalDescriptionLanguageInput.value);
    }
}
var animalDescriptionTranslations = document.getElementById('animal_description_translations');
var animalDescriptionAddTranslation = document.getElementById('animal_add_description_translation');
animalDescriptionAddTranslation.onclick = function(){

    // If creating
    if (animalDetailAnimal.id == null){

        // Add translation to list of displayed translations
        bjørneparkappen.adminconsole.addTranslation(animalDetailAnimal, 'description', animalDescriptionInput, animalDescriptionLanguageInput, animalDescriptionTranslations, animalDescriptionAddTranslation);

    } else {    // Else if updating

        // Update existing translation
        bjørneparkappen.adminconsole.updateTranslation(animalDetailAnimal, 'description', animalDescriptionInput.value, animalDescriptionLanguageInput.value);
    }
}
var animalSpeciesInput = document.getElementById('animal_species');
var animalEnclosureInput = document.getElementById('animal_enclosure');
var animalAvailableInput = document.getElementById('animal_available');

// Clears the Animal Detail page
bjørneparkappen.adminconsole.animals.detail.clearPageData = function(){

    // Reset animal object
    animalDetailAnimal = {};

    // Clear translation inputs and re-enable associated controls
    bjørneparkappen.adminconsole.clearTranslations(animalDescriptionInput, animalDescriptionLanguageInput, animalDescriptionTranslations, animalDescriptionAddTranslation);

    // Clear the form
    animalDetailForm.reset();

    // Clear species dropdown
    animalSpeciesInput.length = 0;

    // Add species options to dropdown
    for (speciesCount = 0; speciesCount < speciesList.length; speciesCount++){

        var option = document.createElement("option");
        option.value = speciesList[speciesCount].id;
        option.text = bjørneparkappen.adminconsole.getTranslation(speciesList[speciesCount], 'common_name', LANGUAGE_CODE);

        animalSpeciesInput.add(option);
    }

    animalSpeciesInput.disabled = false;

    // Clear enclosures dropdown
    animalEnclosureInput.length = 0;

    // Add enclosure options to dropdown
    for (enclosureCount = 0; enclosureCount < enclosuresList.length; enclosureCount++){

        var option = document.createElement("option");
        option.value = enclosuresList[enclosureCount].id;
        option.text = bjørneparkappen.adminconsole.getTranslation(enclosuresList[enclosureCount], 'label', LANGUAGE_CODE);

        animalEnclosureInput.add(option);
    }
};

// Load the Create Animal page
bjørneparkappen.adminconsole.animals.detail.loadCreatePage = function(){

    // Clear page data
    bjørneparkappen.adminconsole.animals.detail.clearPageData();

    // Set page title and confirm button to create
    animalDetailTitle.innerHTML = "Create Animal";
    animalDetailConfirm.value = "Create";
    animalDescriptionAddTranslation.innerHTML = "Add";

    animalDetailConfirm.onclick = function(){

        animalDetailAnimal.name = animalNameInput.value;
        animalDetailAnimal.enclosure_id = animalEnclosureInput.value;
        animalDetailAnimal.is_available = animalAvailableInput.checked;

        if (bjørneparkappen.adminconsole.animals.detail.validate()) {

            bjørneparkappen.adminconsole.api.createAnimal(animalDetailAnimal);
        }
    }

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
    animalDescriptionAddTranslation.innerHTML = "Update";
    animalSpeciesInput.disabled = true;

    // Set animal object
    animalDetailAnimal = animal;

    animalDetailConfirm.onclick = function(){

        animalDetailAnimal.name = animalNameInput.value;
        animalDetailAnimal.enclosure_id = animalEnclosureInput.value;
        animalDetailAnimal.is_available = animalAvailableInput.checked;

        if (bjørneparkappen.adminconsole.animals.detail.validate()) {

            bjørneparkappen.adminconsole.api.updateAnimal(animalDetailAnimal);
        }
    }

    // Populate page with new data
    animalNameInput.value = animalDetailAnimal.name;
    animalSpeciesInput.value = animalDetailAnimal.species.id;
    animalDescriptionInput.value = bjørneparkappen.adminconsole.getTranslation(animalDetailAnimal, 'description', animalDescriptionLanguageInput.value);
    animalEnclosureInput.value = animalDetailAnimal.enclosure_id;
    animalAvailableInput.checked = animalDetailAnimal.is_available;

    // Display the detail page
    bjørneparkappen.adminconsole.navigation.displayPage(animalDetailPage);
}

// Validates detail input
bjørneparkappen.adminconsole.animals.detail.validate = function(){

    var valid = false;

    if (animalDetailAnimal.name == ""){

        alert("Please enter a name.")

    } else if (animalDetailAnimal.species_id == ""){

        alert("Please enter a species.");

    } else if (animalDetailAnimal.description.length < animalDescriptionLanguageInput.length) {

        alert("Please enter all translations for the animal description.");

    } else if (animalDetailAnimal.enclosure_id == ""){

        alert("Please enter an enclosure.");

    } else {

        valid = true;
    }

    return valid;
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
    for (i = 0; i < amenitiesList.length; i++){

        var option = document.createElement("option");
        option.value = amenitiesList[i].id;
        option.text = amenitiesList[i].label;

        eventAreaInput.add(option);
    }

    // Add enclosure options to dropdown
    for (i = 0; i < enclosuresList.length; i++){

        var option = document.createElement("option");
        option.value = enclosuresList[i].id;
        option.text = enclosuresList[i].label;

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
var keepersList = [];

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

    // If no keepers returned
    if (keepersList.length == 0) {

        // Retrieve list of keepers
        bjørneparkappen.adminconsole.api.getKeepers();

    } else {

        // Iterate through each one and add it to the table
        for (var i = 0; i < keepersList.length; i++) {

            // Add keeper to table
            bjørneparkappen.adminconsole.keepers.addToTable(keepersList[i]);
        }
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
    bioCell.innerHTML = bjørneparkappen.adminconsole.getTranslation(keeper, 'bio', LANGUAGE_CODE);

    // Create link to delete item
    var deleteLink = document.createElement('a');
    deleteLink.href = '#';
    deleteLink.innerHTML = "Delete"
    deleteLink.onclick = function(){

        bjørneparkappen.adminconsole.keepers.delete(keeper);
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

    if (confirm("Are you sure?")){

        bjørneparkappen.adminconsole.api.deleteKeeper(keeper);
    }
}

// Keeper Detail Page and elements
var keeperDetailKeeper = {};
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
var keeperBioLanguageInput = document.getElementById('keeper_bio_language');
keeperBioLanguageInput.onchange = function(){

    if (keeperDetailKeeper.id != null){

        // Update textbox value
        keeperBioInput.value = bjørneparkappen.adminconsole.getTranslation(keeperDetailKeeper, 'bio', keeperBioLanguageInput.value);
    }
}
var keeperBioTranslations = document.getElementById('keeper_bio_translations');
var keeperBioAddTranslation = document.getElementById('keeper_add_bio_translation');
keeperBioAddTranslation.onclick = function(){

    // If creating
    if (keeperDetailKeeper.id == null){

        // Add translation to list of displayed translations
        bjørneparkappen.adminconsole.addTranslation(keeperDetailKeeper, 'bio', keeperBioInput, keeperBioLanguageInput, keeperBioTranslations, keeperBioAddTranslation);

    } else {    // Else if updating

        // Update existing translation
        bjørneparkappen.adminconsole.updateTranslation(keeperDetailKeeper, 'bio', keeperBioInput.value, keeperBioLanguageInput.value);
    }
}

// Clears the Keeper Detail page
bjørneparkappen.adminconsole.keepers.detail.clearPageData = function(){

    // Reset keeper object
    keeperDetailKeeper = {};

    // Clear translation inputs and re-enable associated controls
    bjørneparkappen.adminconsole.clearTranslations(keeperBioInput, keeperBioLanguageInput, keeperBioTranslations, keeperBioAddTranslation);

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
    keeperBioAddTranslation.innerHTML = "Add";

    keeperDetailConfirm.onclick = function(){

        keeperDetailKeeper.name = keeperNameInput.value;

        if (bjørneparkappen.adminconsole.keepers.detail.validate()) {

            bjørneparkappen.adminconsole.api.createKeeper(keeperDetailKeeper);
        }
    }

    // Display the detail page
    bjørneparkappen.adminconsole.navigation.displayPage(keeperDetailPage);
}

// Loads the Update Keeper page
bjørneparkappen.adminconsole.keepers.detail.loadUpdatePage = function(keeper){

    // Clear page data
    bjørneparkappen.adminconsole.keepers.detail.clearPageData();

    // Set page title and confirm button to update
    keeperDetailTitle.innerHTML = "Update Keeper";
    keeperDetailConfirm.value = "Save";
    keeperBioAddTranslation.innerHTML = "Update";

    // Set keeper object
    keeperDetailKeeper = keeper;

    keeperDetailConfirm.onclick = function(){

        keeperDetailKeeper.name = keeperNameInput.value;

        if (bjørneparkappen.adminconsole.keepers.detail.validate()) {

            bjørneparkappen.adminconsole.api.updateKeeper(keeperDetailKeeper);
        }
    }

    keeperNameInput.value = keeper.name;
    keeperBioInput.value = bjørneparkappen.adminconsole.getTranslation(keeperDetailKeeper, 'bio', LANGUAGE_CODE);

    // Display the detail page
    bjørneparkappen.adminconsole.navigation.displayPage(keeperDetailPage);
}

// Validates detail input
bjørneparkappen.adminconsole.keepers.detail.validate = function(){

    var valid = false;

    if (keeperDetailKeeper.name == ""){

        alert("Please enter a name.")

    } else if (keeperDetailKeeper.bio.length < keeperBioLanguageInput.length) {

        alert("Please enter all translations for the keeper bio.");

    } else {

        valid = true;
    }

    return valid;
}
