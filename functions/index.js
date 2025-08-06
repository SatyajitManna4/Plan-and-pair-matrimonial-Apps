const {onDocumentCreated} = require("firebase-functions/v2/firestore");
const {initializeApp} = require("firebase-admin/app");
const {getFirestore} = require("firebase-admin/firestore");
const {getMessaging} = require("firebase-admin/messaging");

initializeApp();

exports.sendLikeNotification = onDocumentCreated(
    "UsersData/{userId}/Notifications/{notifierId}",
    async (event) => {
      const notification = event.data?.data();
      const userId = event.params.userId;

      if (!notification || !userId) return;

      const userDoc = await getFirestore()
          .collection("UsersData")
          .doc(userId)
          .get();

      const token = userDoc.data().fcmToken;
      if (!token) return null;

      const payload = {
        notification: {
          title: "PlanPair",
          body: notification.message,
          image: notification.imageUrl || "",
        },
        token: token,
      };

      return getMessaging().send(payload);
    },
);

// const functions = require("firebase-functions");
// const admin = require("firebase-admin");
// admin.initializeApp();

// exports.sendMatchNotification = functions.firestore
//     .document("UsersData/{uid}/Likes/{likedUid}")
//     .onCreate(async (snap, context) => {
//       const likedUid = context.params.likedUid;
//       const uid = context.params.uid;

//       const likedUserDoc = await admin
//           .firestore()
//           .collection("UsersData")
//           .doc(likedUid)
//           .get();

//       const token = likedUserDoc.get("fcmToken");

//       if (!token) return;

//       const payload = {
//         notification: {
//           title: "You have a new like!",
//           body: "Someone liked your profile on PlanPair.",
//           click_action: "FLUTTER_NOTIFICATION_CLICK",
//         },
//       };

//       await admin.messaging().sendToDevice(token, payload);
//     });
