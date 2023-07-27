| Date       | Sidharth | Ryan | Aditya | Prisha | Emily | Sayma | Task                             |
|------------|----------|------|--------|--------|-------|-------|----------------------------------|
|05/19/2023 |2|2|2|2|2|2|Brain storming ideas session|
|06/02/2023|0|0|0|0|0|2|Created mock-ups for UI of different key features|
|06/24/2023|0|0|0|8|0|0|Implemented working skeleton of app and allowed easy access of including new acitivities/features|
|06/24/2023|0|0|16|0|0|0|Created Notes scanning functionality using androidMLkit api in Java (contributions not visible for this as email was not configured) |
|06/24/2023|0|8|0|0|0|0| Set up + Created initial skeleton and frontend for calendar code
|06/25/2023|7|0|0|0|0|0|Converting Note Scanner code from Java to Kotlin and integrating with temporary dashboard for demoing purposes|
|06/25/2023|0|0|0|0|8|0|Set up firebase for project and set up google authentication (contributions not visible for this as email was not configured but shows if in commits with my name)|
|06/25/2023|0|0|0|0|4|0|Integrated google auth with existing UI (contributions not visible for this as email was not configured but shows if in commits with my name)|
|06/25/2023|0|2|0|0|0|0|Resolving merges with google authentication and calendar|
|06/25/2023|0|0|0|3|0|0|Created splash page for application and modified to match mockups|
|06/25/2023|0|0|0|4|0|0|Created login activity inlcuding allowing users to type in username and password and verify. It also checked for password conditions (shown in commit)|
|06/27/2023|0|0|0|0|0|5|Set up initial dashboard interface and calendar UI (front-end)|
|06/28/2023|0|0|0|0|0|2|Continue calendar (dark scheme) and logout UI|
|07/10/2023|4|0|0|0|0|0|Setup firestore for Ace Project and setup codebase to access firestore from Kotlin files|
|07/18/2023|0|2|0|0|0|0|Fixed bugs where calendar and camera were closing on 'Back'|
|07/18/2023|5|0|0|0|0|0|Extracted data from authentication to get UID from firebase, in order to setup firestore. Update dashboard from ConstraintLayout to LinearLayout for easier future developement|
|07/19/2023|0|0|1|0|0|0|Updated email for project, contributions visible and updated image assets|
|07/19/2023|0|2|0|0|0|0|Added Profile to Dashboard + General UI fixes|
|07/20/2023|5|0|0|0|0|0|Create document and collection infrastructure for fellow team members to store data on firebase such as calendar events and messaging services. Review Pull Requests and debug camera scanner crashing|
|07/21/2023|5|0|0|0|0|0|Developed Dialog Window and three splash screens to host all grade content.|
|07/21/2023|0|0|0|15|0|0|Implemented frontend chat messaging functionality allowing users to send and recieve test messages. |
|07/21/2023|0|0|0|4|0|0|Allowed users to select contacts to chat with. (Seperated chats for each contacts) |
|07/21/2023|0|0|0|3|0|0|Fixed issues in Android Manifest |
|07/21/2023|0|8|0|0|0|0|Frontend updates for profile|
|07/21/2023|0|0|0|0|8|0|Implemented backend to query firestore to get all authenticated users to display as chat contacts. Redesigned chat messages to store sender and receiver's google display name|
|07/22/2023|8|0|0|0|0|0|Create additional pages to keep track of user grades. Link syllabus grades to Profile View. Create No Classes added messages. Fix UI bugs|
|07/22/2023|0|0|0|0|16|0|Set up sending messages to be stored in realtime in firebase. Created backend to retreive each message and display only to specifc sender/receiver|
|07/22/2023|0|2|0|0|0|0|Updates to profile UI + AndroidManifest|
|07/22/2023|10|0|0|0|0|0|Created UI and data retrieval from Firestore for grades and classes for a user|
|07/22/2023|0|0|0|14|0|0|Implemented frontend discussion forum functionality allowing grouped users to send and recieve test messages.|
|07/22/2023|0|0|0|3|0|0|Allowed users to select topics to dicuss on. (Seperated topics for each discussion forum)|
|07/22/2023|0|0|10|0|0|0|Experiment: PDF generation of scanned image to text directly to phone's internal storage. Result -> Permission error (SDK 33 issue) (legacy code in pdf_feature branch)|
|07/22/2023|0|0|0|0|0|6|Design main page UX, design icons, and refactor activity for functionality|
|07/23/2023|13|0|0|0|0|0|Created custom card components to showcase Grades and messaging features. Finalized firestore infrastructure to be utilized throughout app. Implemented hot reloading from firestore. Added the math logic for grades, syllabus grades, class grades and cummalitive grade. Updated color theme of app. Add /drawable assets to be used in application by fellow team members|
|07/23/2023|0|0|0|0|5|0|Integrated chat backend with existing frontend|
|07/23/2023|0|0|10|0|0|0|Created Firebase Storage to directly upload generated pdfs from app. Implemented UI to allow seamless access to pdf content directly downloaded to internal storage from firebase|
|07/23/2023|0|16|0|0|0|0|Profile UI edits + horizontal scrolling for course cards + Begin fetching backend data to load courses from user's firebase into the profile UI and render cards based on what courses they take + route cards to grades pages onClick|
|07/23/2023|0|0|0|0|10|0|Designed and created new discussions collection in firebase realtime database to store all discussion messages and added the code to store these messages on send in realtime|
|07/24/2023|3|0|0|0|0|0|Fix small bugs reported by team members throughout UI|
|07/24/2023|0|0|0|0|8|0|Implemented backend to query firestore for which classes a user is in to be displayed on the discussions' topics page. Integrated this with the existing UI|
|07/24/2023|0|0|0|0|0|6|Make calendar interface close to mock-up schemes and create front-end/back-end for study recommendations based on grade performance|
|07/24/2023|0|13|0|0|0|0|Added more backend functionality for profile UI + load user's name into the profile + general bug fixes|
|07/24/2023|0|0|15|0|0|0|Redesigned the CameraActivity UI to match the theme. Fixed bugs and polished the transitions for pdf downloads using dialog boxes.  
|07/24/2023|0|0|0|9|0|0|Redesigned chat messaging, contact list, discussion forum, discussion topics UI layout|
|07/25/2023|15|0|0|0|0|0|Update insights modal to not show incorrect insights for classes that have just been created. Fix bugs within Chat and Forum pages. Update chat and Forum UI to be more modern and replicate mockups. Create a counter for the number of students in a class, so that this can be displayed in the forums page. Port the logic from Grades where we add classes, over to the Profile page to handle this. Clean up UI for demo. Create hyperlink from all pages that need classes, over to the Profile page when there are no classes. Add the ability to edit a users displayName (later to be removed for security)|
|07/25/2023|0|0|0|0|10|0|Redesigned discussion messages to store minimal fields. Coded the backend to show sent messages and all incoming received messages from any authenticated user in their class in realtime. Fixed bugs in receiving messages list and making sure only enrolled users were able to message in the discussion|
|07/25/2023|0|0|0|4|0|0|Added sent notifications to indicate user's message is successfully sent and added navigation back to dashboard from chat contacts and discussion forum topics. (Was removed in favor of navigation bar but commits are shown)|
|07/25/2023|0|0|0|3|0|0|Remodeled profile layout and matched UI with rest of the application|
|07/25/2023|0|0|0|0|0|15|Make UI fit on multiple devices, enhance performance insights graphics, update profile UI to match mock-ups, add ability to export user calendar into .ics file that directly loads into Google Calendar account, code profile UI to match application|
|07/25/2023|0|0|0|0|6|0|Added a bad word filter to prevent harrassment in direct messages/discussions. General bug fixes in chat/messaging features|
|07/25/2023|0|10|0|0|0|0|Expanded on backend functionality for the pdf scanner so that the link is shared between activities|
|07/25/2023|0|0|10|0|0|0|Experiment: Tried implementing push notifications for the chat message feature. Device tokens are not available and notification permissions required for SDK's later than Android Oreo. Code available on 'notification' branch|
|07/26/2023|4|0|0|0|0|0|Update UI and colors, as well as messages in the Notes Scanner to prepare it for Demo|
|07/26/2023|0|5|0|0|0|0|Load notes into profile UI + Debugging card issues + Update Profile UI to support notes|
|07/26/2023|0|0|10|0|0|0|UI enhancement for notes section in profile view. Debugging UI crash for notes app. Fix: User must wait until the Toast message shows up before clicking 'yes' in the dialog box as this indicates that the file has been uploaded to Firebase Storage successfully. Internal storage PDF viewer error: Android PDF viewer may not work always, if it does not work, user must use 'print' option in the file to view the pdf version of the text (This is an Android issue)|
