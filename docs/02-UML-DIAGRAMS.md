# Ocean View Resort — UML Diagrams (Professional)

This document describes the **Use Case**, **Class**, and **Sequence** diagrams for the Ocean View Resort Online Room Reservation System. All use cases are connected to actors and related use cases using **association**, **<<include>>**, **<<extend>>**, and **generalization** where appropriate.

---

## 1. Use Case Diagram

### 1.1 Purpose

The Use Case Diagram shows **actors** (who uses the system), **use cases** (what the system does), and the **relationships** between them. Every use case is linked to at least one actor by an **association** (solid line). Optional or alternative flows are shown with **<<extend>>**; mandatory sub-functions with **<<include>>**.

### 1.2 Actors (Assignment: Customer, Staff, Manager)

| Actor | Description |
|-------|-------------|
| **Customer** | End user who browses rooms, makes reservations, views booking history, and uses help. |
| **Staff** | Front-desk or operations: manages bookings, uploads room images, assists customers. |
| **Manager** | Oversees system: manages rooms (add/edit/delete), views reports, full administrative access. |

*Mapping to implementation:* In the system, Customer = guest user (role USER); Staff and Manager = users with role ADMIN (Staff may have limited admin actions; Manager has full access). For the diagram, all three are shown as separate actors so every use case is clearly linked to the correct role.

### 1.3 Use Cases and Actor Associations

**Every use case is connected to one or more actors** by an association (solid line from actor to use case).

#### Customer (primary actor)

| Use Case | Description | Association |
|----------|-------------|-------------|
| Register | Create account (name, email, password, phone). | Customer ——→ Register |
| Login | Authenticate with email and password. | Customer ——→ Login |
| Browse Rooms | View room list with filters (price, type, sea view). | Customer ——→ Browse Rooms |
| View Room Details | See description, amenities, images, price. | Customer ——→ View Room Details |
| Add New Reservation | Store guest and booking details; get reservation number. | Customer ——→ Add New Reservation |
| Display Reservation Details | Retrieve and show full booking info for a reservation. | Customer ——→ Display Reservation Details |
| Calculate and Print Bill | Compute total (nights × rate) and print. | Customer ——→ Calculate and Print Bill |
| View Booking History | List past and upcoming bookings. | Customer ——→ View Booking History |
| View Help | Read guidelines (help section). | Customer ——→ View Help |
| Exit System | Logout; end session safely. | Customer ——→ Exit System |

#### Staff (primary actor)

| Use Case | Description | Association |
|----------|-------------|-------------|
| Login (Admin) | Authenticate as staff. | Staff ——→ Login (Admin) |
| Manage Bookings | View, update status, cancel bookings. | Staff ——→ Manage Bookings |
| Upload Room Images | Attach images to rooms. | Staff ——→ Upload Room Images |
| View Help | Read guidelines for staff. | Staff ——→ View Help |
| Exit System | Logout; end session. | Staff ——→ Exit System |

#### Manager (primary actor)

| Use Case | Description | Association |
|----------|-------------|-------------|
| Login (Admin) | Authenticate as manager. | Manager ——→ Login (Admin) |
| Manage Rooms | Add, update, delete rooms. | Manager ——→ Manage Rooms |
| Upload Room Images | Attach images to rooms. | Manager ——→ Upload Room Images |
| Manage Bookings | View, update status, cancel bookings. | Manager ——→ Manage Bookings |
| View Reports | Booking reports (revenue, occupancy). | Manager ——→ View Reports |
| View Help | Read guidelines. | Manager ——→ View Help |
| Exit System | Logout; end session. | Manager ——→ Exit System |

### 1.4 Relationships Between Use Cases (Optimal Notations)

#### <<include>> (mandatory sub-function)

- **Add New Reservation** <<include>> **Check Availability**  
  *Making a reservation always includes checking that the room is free for the chosen dates.*

- **Add New Reservation** <<include>> **Calculate Total**  
  *Reservation always includes computing total cost (nights × room rate).*

- **Add New Reservation** <<include>> **Generate Reservation Number**  
  *Every new reservation gets a unique reservation number.*

- **Login** <<include>> **Validate Credentials**  
  *Login always includes validating email and password.*

- **Display Reservation Details** <<include>> **Retrieve Booking**  
  *Showing reservation details always includes fetching the booking from the system.*

- **Manage Rooms** <<include>> **Persist Room Data**  
  *Adding/updating rooms always includes saving to the database.*

#### <<extend>> (optional extension)

- **Cancel Booking** <<extend>> **View Booking History**  
  *Cancellation is an optional action when viewing booking history.*

- **Print Bill** <<extend>> **Display Reservation Details**  
  *Printing the bill is an optional action when viewing reservation details.*

#### Generalization (optional, for report)

- **Add New Reservation** can be shown as the concrete use case for the assignment requirement “Add New Reservation”.
- **Display Reservation Details** and **Calculate and Print Bill** map to assignment requirements 3 and 4; **View Help** and **Exit System** map to 5 and 6.

### 1.5 Summary: All Connections

- **Actors → Use cases:** Each use case has at least one **association** from an actor (Customer, Staff, or Manager).
- **Use case → Use case (include):** Base use cases **include** mandatory sub–use cases (Check Availability, Calculate Total, Generate Reservation Number, Validate Credentials, Retrieve Booking, Persist Room Data).
- **Use case → Use case (extend):** Optional use cases **extend** base use cases (Cancel Booking extends View Booking History; Print Bill extends Display Reservation Details).

This satisfies the requirement: *“All the use cases need to be connected with actors or derived use cases using optimal relationship notations.”*

---

## 2. Class Diagram (Main Classes)

### 2.1 Domain Entities

| Class | Responsibility |
|-------|----------------|
| **User** | Guest or admin identity; name, email, password, phone, role. |
| **Room** | Room type, price, amenities, availability. |
| **Booking** | Reservation linking user and room; dates, total price, status. |
| **Admin** | (Optional) Separate admin entity; in this system admin is a User with role ADMIN. |

### 2.2 Class Details (UML Style)

**User**
- `- userId : int`
- `- name : String`
- `- email : String`
- `- password : String`
- `- phone : String`
- `- role : String`
- `+ getters/setters`

**Room**
- `- roomId : int`
- `- roomName : String`
- `- roomType : String`
- `- pricePerNight : double`
- `- description : String`
- `- hasSeaView : boolean`
- `- hasWifi : boolean`
- `- hasAC : boolean`
- `- hasPoolAccess : boolean`
- `- breakfastIncluded : boolean`
- `- imagePath : String`
- `+ getters/setters`

**Booking**
- `- bookingId : int`
- `- reservationNumber : String`
- `- userId : int`
- `- roomId : int`
- `- checkIn : LocalDate`
- `- checkOut : LocalDate`
- `- totalPrice : double`
- `- status : String`
- `+ getters/setters`

### 2.3 Relationships

- **User** 1 —— * **Booking** (one user has many bookings; association).
- **Room** 1 —— * **Booking** (one room has many bookings; association).
- **Booking** ——→ **User** (many-to-one); **Booking** ——→ **Room** (many-to-one).

### 2.4 Extended Class Diagram (Assignment-Style: Inheritance and Extra Classes)

For the report you may show an **extended** domain model with inheritance and additional concepts:

| Class | Responsibility | Relationship |
|-------|-----------------|--------------|
| **User** | Base: userId, name, email, password, phone. | Abstract/superclass. |
| **Customer** | Books rooms, views reservations. | Inherits User. |
| **Staff** | Manages bookings, uploads images. | Inherits User. |
| **Manager** | Manages rooms, views reports. | Inherits User. |
| **Room** | Room type, price, amenities. | 1 —— * Reservation. |
| **Reservation** | Same as Booking: reservation number, dates, total, status. | Many-to-one User, Room. |
| **Payment** | Optional: amount, method, status; linked to Reservation. | * —— 1 Reservation. |
| **Bill** | Computed view: nights × rate, total; can be printed. | Derived from Reservation. |
| **Report** | Aggregated data (revenue, occupancy) for Manager. | Uses Reservation / Room data. |

**Inheritance:** User △—— Customer, Staff, Manager (generalization: hollow triangle on User side).  
**Associations:** Customer/Staff/Manager 1 —— * Reservation; Room 1 —— * Reservation; Reservation 1 —— * Payment (optional); Report uses Reservation and Room.

*Implementation note:* In the current system, Customer/Staff/Manager are represented by **User** with a **role** field (USER vs ADMIN) rather than subclasses; the extended diagram is for the assignment’s conceptual model.

### 2.5 Supporting Classes (MVC / 3-Tier)

- **UserDAO**, **RoomDAO**, **BookingDAO**: data access; depend on **DBConnection**.
- **UserService**, **RoomService**, **BookingService**: business logic; use DAOs.
- **Controllers (Servlets)**: use Services; set request/session and forward to **Views (JSP)**.

---

## 3. Sequence Diagram — Room Booking Process

**Purpose:** Shows the interaction between actor and system objects when a guest performs **Add New Reservation** (with included use cases Check Availability, Calculate Total, Generate Reservation Number).

**Participants:** Customer (Actor), Browser, BookingServlet (Controller), BookingService, RoomService, BookingDAO, RoomDAO, Database.

**Flow:**

1. Customer submits booking form (roomId, check-in, check-out) from Browser.
2. BookingServlet receives request; validates input (dates, roomId).
3. BookingServlet calls **BookingService.createBooking(userId, roomId, checkIn, checkOut)**.
4. BookingService calls **RoomService** (or RoomDAO) to get room price.
5. BookingService calls **BookingDAO** to check conflicting bookings (check-in/check-out range).
6. BookingDAO queries Database; returns conflicts.
7. If conflict: BookingService returns error; Controller sets message and forwards back to form.
8. If no conflict: BookingService **calculates total** (nights × price), **generates reservation number**, then calls **BookingDAO.insert(booking)**.
9. BookingDAO inserts into Database.
10. BookingService returns created Booking (with reservation number).
11. BookingServlet redirects to confirmation page (View).
12. Browser displays confirmation (reservation number, summary, total); Customer sees **Display Reservation Details** and may **Print Bill** (extend).

This sequence aligns with the Use Case **Add New Reservation** and its <<include>> use cases.

---

## 4. How to Draw the Diagrams for Your Report

- **Tool:** Use Draw.io, Lucidchart, PlantUML, or Visio.
- **Use Case:** Draw a system boundary (rectangle) labeled “Ocean View Resort Reservation System”. Place actors (stick figures) outside: **Customer**, **Staff**, **Manager**. Place all use cases inside. Draw **association** lines from each actor to each of their use cases. Draw **<<include>>** (dashed arrow from base to included) and **<<extend>>** (dashed arrow from extending to base).
- **Class:** Draw classes with three compartments (name, attributes, methods). Draw associations (lines with multiplicities 1, *).
- **Sequence:** Draw lifelines (vertical dashed lines) for each participant; messages (horizontal arrows) in order of time; activation boxes where needed.

PlantUML source for all three diagrams is in **`docs/diagrams/`**:

- **`docs/diagrams/use-case.puml`** — Use case diagram (actors, use cases, associations, <<include>>, <<extend>>).
- **`docs/diagrams/class-diagram.puml`** — Class diagram (User, Room, Booking).
- **`docs/diagrams/sequence-booking.puml`** — Sequence diagram (Add New Reservation).

See **`docs/diagrams/README.md`** for how to generate PNG/SVG from these files for your report.

---

## 5. Design Decisions and Assumptions

- **Reservation number:** Unique string (e.g. OV-YYYYMMDD-XXX) generated when a booking is created.
- **Availability:** No overlapping bookings for the same room (check-in/check-out).
- **Roles:** Guest and Administrator; implemented via User.role (e.g. USER, ADMIN).
- **Bill:** Total = (checkOut − checkIn in days) × room price per night; “Print Bill” is an extend of Display Reservation Details.
- **Help / Exit:** “View Help” and “Exit System” are use cases connected to Guest and Administrator.

These diagrams and relationships meet the lecturer’s requirement for professional UML and for connecting all use cases to actors and derived use cases with optimal relationship notations.

---

## 6. What Was Wrong and How It Was Fixed (Lecturer Feedback)

**Feedback:** *“All the use cases need to be connected with actors or derived use cases using optimal relationship notations.”*

### What was wrong

1. **Use cases not explicitly connected to actors**  
   The diagram (or its description) did not show a clear **association** (solid line) from each actor to each use case. In UML, every use case must be linked to at least one actor that initiates it.

2. **Missing or unclear relationship notations**  
   - **<<include>>** was not consistently used for mandatory sub-functions (e.g. “Add New Reservation” must include “Check Availability”, “Calculate Total”, “Generate Reservation Number”).  
   - **<<extend>>** was not clearly used for optional behaviour (e.g. “Cancel Booking” extends “View Booking History”; “Print Bill” extends “Display Reservation Details”).

3. **Actors not aligned with assignment**  
   The assignment requires **Customer**, **Staff**, and **Manager**. The original diagram used only Guest and Administrator, so the actor–use case links did not match the required roles.

4. **Derived use cases not shown**  
   “Derived” use cases (sub-behaviours such as Check Availability, Calculate Total, Validate Credentials) were not shown as separate use cases with **<<include>>** from the base use cases.

### How it was fixed

1. **Every use case is connected to an actor**  
   Each use case now has at least one **association** from Customer, Staff, or Manager. The document and PlantUML diagram list these associations explicitly (e.g. Customer → Register, Customer → Add New Reservation, Manager → View Reports).

2. **<<include>> used for mandatory processes**  
   - Add New Reservation **<<include>>** Check Availability, Calculate Total, Generate Reservation Number.  
   - Login **<<include>>** Validate Credentials.  
   - Display Reservation Details **<<include>>** Retrieve Booking.  
   - Manage Rooms **<<include>>** Persist Room Data.

3. **<<extend>> used for optional processes**  
   - Cancel Booking **<<extend>>** View Booking History.  
   - Print Bill **<<extend>>** Display Reservation Details.

4. **Three actors: Customer, Staff, Manager**  
   Use cases are assigned to the correct actor: Customer (reservations, help, exit); Staff (manage bookings, upload images, help, exit); Manager (manage rooms, reports, and all staff actions).

5. **Clear system boundary**  
   The system boundary is labeled “Ocean View Resort Reservation System”; all use cases sit inside it; actors sit outside and connect in with association lines.

6. **Class and sequence diagrams aligned**  
   The Class Diagram shows domain entities (User, Room, Booking) and their relationships. The Sequence Diagram shows the **Add New Reservation** flow with Controller → Service → DAO → Database, matching the include use cases (Check Availability, Calculate Total, Generate Reservation Number).

The redesigned Use Case Diagram and supporting Class and Sequence diagrams now follow UML best practices and satisfy the requirement that all use cases are connected to actors or derived use cases using optimal relationship notations.
