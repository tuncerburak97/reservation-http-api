// MongoDB Initialization Script
// This script will be executed when MongoDB container starts for the first time

// First, create a user for reservation_db database
db = db.getSiblingDB("reservation_db");

// Create application user for reservation_db
db.createUser({
  user: "reservation_user",
  pwd: "reservation_pass",
  roles: [
    {
      role: "readWrite",
      db: "reservation_db",
    },
  ],
});

print("Created reservation_user for reservation_db database");

// Create collections (optional - they will be created automatically when first document is inserted)
db.createCollection("users");
db.createCollection("owners");
db.createCollection("businesses");
db.createCollection("reservations");
db.createCollection("reservation_settings");
db.createCollection("business_availability");

// Create indexes manually (in case auto-index creation doesn't work)
print("Creating indexes...");

// Users collection indexes
db.users.createIndex({ email: 1 }, { unique: true });
db.users.createIndex({ gsm: 1 });

// Owners collection indexes
db.owners.createIndex({ email: 1 }, { unique: true });
db.owners.createIndex({ gsm: 1 });
db.owners.createIndex({ ownerType: 1 });

// Businesses collection indexes
db.businesses.createIndex({ name: 1 });
db.businesses.createIndex({ name: "text" }, { weights: { name: 2 } });
db.businesses.createIndex({ "owner.id": 1 });
db.businesses.createIndex({ "location.googleId": 1 });

// Reservations collection indexes
db.reservations.createIndex({ "user.id": 1 });
db.reservations.createIndex({ "business.id": 1 });
db.reservations.createIndex({ reservationDate: 1 });
db.reservations.createIndex({ status: 1 });
db.reservations.createIndex({ isConfirmed: 1 });
db.reservations.createIndex({ isCancelled: 1 });
db.reservations.createIndex({ createdAt: 1 });

// Compound indexes for reservations
db.reservations.createIndex(
  {
    "business.id": 1,
    reservationDate: 1,
    "timeSlot.startTime": 1,
  },
  {
    unique: true,
    name: "business_date_timeslot_unique",
  }
);
db.reservations.createIndex(
  { "user.id": 1, reservationDate: 1 },
  { name: "user_date" }
);
db.reservations.createIndex(
  { "business.id": 1, reservationDate: 1 },
  { name: "business_date" }
);

// Reservation Settings collection indexes
db.reservation_settings.createIndex({ businessId: 1 }, { unique: true });

// Business Availability collection indexes
db.business_availability.createIndex({ businessId: 1 });
db.business_availability.createIndex({ availabilityType: 1 });
db.business_availability.createIndex({ dayOfWeek: 1 });
db.business_availability.createIndex({ specificDate: 1 });
db.business_availability.createIndex({ startDate: 1, endDate: 1 });
db.business_availability.createIndex({ isActive: 1 });

// Compound index for business availability
db.business_availability.createIndex(
  {
    businessId: 1,
    availabilityType: 1,
    dayOfWeek: 1,
  },
  { name: "business_availability_weekly" }
);
db.business_availability.createIndex(
  {
    businessId: 1,
    specificDate: 1,
  },
  { name: "business_availability_date" }
);

print("Database and indexes created successfully!");
print("Collections created:");
print("- users");
print("- owners");
print("- businesses");
print("- reservations");
print("- reservation_settings");
print("- business_availability");

// Insert sample data (optional)
/*
print('Inserting sample data...');

// Sample Owner
db.owners.insertOne({
    name: "Admin",
    lastname: "User",
    gsm: "05551234567",
    email: "admin@reztech.com",
    ownerType: "ADMIN"
});

print('Sample data inserted successfully!');
*/

print("MongoDB initialization completed!");
