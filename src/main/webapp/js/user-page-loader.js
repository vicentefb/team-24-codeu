/*
 * Copyright 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// Get ?user=XYZ parameter value
const urlParams = new URLSearchParams(window.location.search);
const parameterUsername = urlParams.get('user');

// URL must include ?user=XYZ parameter. If not, redirect to homepage.
if (!parameterUsername) {
  window.location.replace('/');
}

/**
 * Shows the message form if the user is logged in and viewing their own page, sets input data to 
 * read only if viewing another user's page, removes hidden value from inputs
 */
function settingsIfViewingSelf() {
  fetch('/login-status')
      .then((response) => {
        return response.json();
      })
      .then((loginStatus) => {
        //email is always read-only and cannot be edited even if you're viewing your own page
        document.getElementById('email-input').readOnly = true;

        if (loginStatus.isLoggedIn && 
          loginStatus.username == parameterUsername) { //this means the user is logged in and viewing their own profile page

          const messageForm = document.getElementById('message-form');
          messageForm.classList.remove('hidden');

          //sets the page header
          document.getElementById('page-title').innerText = "My Profile";
          document.title = parameterUsername + ' - User Page';

        } else { //this means the user is viewing another user's profile page

          //sets all of the input fields to readOnly, so that they can't be edited
          document.getElementById('first-name-input').readOnly = true;
          document.getElementById('last-name-input').readOnly = true;
          document.getElementById('city-input').readOnly = true;
          document.getElementById('state-province-input').readOnly = true;
          document.getElementById('country-input').readOnly = true;
          document.getElementById('about-me-input').readOnly = true;

          //removes the submit changes button
          document.getElementById('submit-button').style.visibility = 'hidden';

          //sets the page title to list the other user's email 
          document.getElementById('page-title').innerText = parameterUsername + "'s Profile";
          document.title = parameterUsername + ' - User Page';
        }
      });

    //removes the hidden attributes from the input boxes  
    document.getElementById('first-name-form').classList.remove('hidden');
    document.getElementById('last-name-form').classList.remove('hidden');
    document.getElementById('city-form').classList.remove('hidden');
    document.getElementById('state-province-form').classList.remove('hidden');
    document.getElementById('country-form').classList.remove('hidden');
    document.getElementById('email-form').classList.remove('hidden');
    document.getElementById('about-me-form').classList.remove('hidden');
}

/** Fetches messages and add them to the page. */
function fetchMessages() {
  const url = '/messages?user=' + parameterUsername;
  fetch(url)
      .then((response) => {
        return response.json();
      })
      .then((messages) => {
        const messagesContainer = document.getElementById('message-container');
        if (messages.length == 0) {
          messagesContainer.innerHTML = '<p>This user has no posts yet.</p>';
        } else {
          messagesContainer.innerHTML = '';
        }
        messages.forEach((message) => {
          const messageDiv = buildMessageDiv(message);
          messagesContainer.appendChild(messageDiv);
        });
      });
}

/**
 * Builds an element that displays the message.
 * @param {Message} message
 * @return {Element}
 */
function buildMessageDiv(message) {
  const headerDiv = document.createElement('div');
  headerDiv.classList.add('message-header');
  headerDiv.appendChild(document.createTextNode(
    message.user + ' - ' + new Date(message.timestamp)));
  const bodyDiv = document.createElement('div');
  bodyDiv.classList.add('message-body');
  bodyDiv.innerHTML = message.text;

  const messageDiv = document.createElement('div');
  messageDiv.classList.add('message-div');
  messageDiv.appendChild(headerDiv);
  messageDiv.appendChild(bodyDiv);

  return messageDiv;
}

/** Uses the fetch() function to request the user's about data, and then replaces the placeholder in the 
 * input box
 */
function fetchAboutMe() {
    const url = '/about?user=' + parameterUsername;
    fetch(url).then((response) => {
        return response.json();
    }).then((myInfoJson) => {
        //retrieves all of the input containers on the profile page
        const firstNameContainer = document.getElementById('first-name-input');
        const lastNameContainer = document.getElementById('last-name-input');
        const cityContainer = document.getElementById('city-input');
        const stateProvinceContainer = document.getElementById('state-province-input');
        const countryContainer = document.getElementById('country-input');
        const emailContainer = document.getElementById('email-input');
        const aboutMeContainer = document.getElementById('about-me-input');

        /* the following statements go through each index in myInfoJson in order of the 
         * textboxes on the page, and if it is blank (meaning no info from user), the 
         * placeholder is kept at the default value, but if there is input from the 
         * user, that information becomes the new placeholder so the user can see what 
         * they have on their profile
         */ 
        if(myInfoJson[0] != "") {
          firstNameContainer.placeholder = myInfoJson[0];
        }

        if(myInfoJson[1] != "") {
          lastNameContainer.placeholder = myInfoJson[1];
        }

        if(myInfoJson[2] != "") {
          cityContainer.placeholder = myInfoJson[2];
        }

        if(myInfoJson[3] != "") {
          stateProvinceContainer.placeholder = myInfoJson[3];
        }

        if(myInfoJson[4] != "") {
          countryContainer.placeholder = myInfoJson[4];
        }

        if(myInfoJson[5] != "") {
          emailContainer.placeholder = myInfoJson[5];
        }

        if(myInfoJson[6] != "") {
          aboutMeContainer.placeholder = myInfoJson[6];
        }
    });
}

/** Fetches data and populates the UI of the page. */
function buildUI() {
  settingsIfViewingSelf();
  fetchMessages();
  fetchAboutMe();
}