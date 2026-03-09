# UML Diagram Sources (PlantUML)

Generate PNG or SVG images for your report using [PlantUML](https://plantuml.com/).

## How to generate

1. **Install PlantUML** (optional): Java is required.  
   - Download `plantuml.jar` from [plantuml.com](https://plantuml.com/download), or  
   - Use the [PlantUML online server](https://www.plantuml.com/plantuml/uml/).

2. **From command line** (with `plantuml.jar` in current directory):
   ```bash
   java -jar plantuml.jar docs/diagrams/use-case.puml
   java -jar plantuml.jar docs/diagrams/class-diagram.puml
   java -jar plantuml.jar docs/diagrams/sequence-booking.puml
   ```
   This creates `.png` files next to each `.puml` file.

3. **From Draw.io / other tools:** Copy the structure from `02-UML-DIAGRAMS.md` (actors, use cases, include, extend) and draw manually if you prefer.

## Files

| File | Diagram | Purpose |
|------|---------|---------|
| `use-case.puml` | Use Case | Actors (Customer, Staff, Manager), all use cases, associations, <<include>>, <<extend>> |
| `class-diagram.puml` | Class | User, Room, Booking and relationships |
| `class-diagram-extended.puml` | Class (extended) | User → Customer/Staff/Manager inheritance; Reservation, Payment, Bill, Report |
| `sequence-booking.puml` | Sequence | Add New Reservation flow (booking process) |

All use cases are connected to actors; include/extend are used as required for the assignment.
