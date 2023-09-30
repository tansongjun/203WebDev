import { DatePicker } from "@gsebdev/react-simple-datepicker";

function Example({ data }) {}

function AppointmentHome() {
  const onChangeCallback = ({ target }) => {
    // a callback function when user select a date
  };
  <DatePicker
    id="datepicker-id"
    name="date-demo"
    onChange={onChangeCallback}
    value={"01/02/2023"}
  />;

  const people = [
    {
      //er this is a sample haha
      id: 0, // Used in JSX as a key
      name: "Creola Katherine Johnson",
      profession: "mathematician",
      accomplishment: "spaceflight calculations",
      imageId: "MK3eW3A",
    },
  ];

  const listItems = people.map((person) => (
    <li key={person.id}>
      <p>
        <b>{person.name}</b>
        {" " + person.profession + " "}
        known for {person.accomplishment}
      </p>
    </li>
  ));

  function generateTimeSlots(startTime, endTime, timeInterval) {
    const timeSlots = [];
    let currentTime = startTime;

    while (currentTime < endTime) {
      timeSlots.push(currentTime);
      // Add the interval to the current time
      currentTime = addMinutes(currentTime, timeInterval);
    }

    return timeSlots;
  }

  function addMinutes(time, minutes) {
    const date = new Date(time);
    date.setMinutes(date.getMinutes() + minutes);
    return date;
  }

  const startTime = new Date(2023, 0, 2, 8, 0); // 8:00 AM
  const endTime = new Date(2023, 0, 2, 11, 0); // 6:00 PM
  const timeInterval = 30; // 30 minutes interval

  const timeSlotsArray = [
    new Date(2023, 0, 2, 8, 0), // 8:00 AM
    new Date(2023, 0, 2, 9, 0), // 9:00 AM
    new Date(2023, 0, 2, 10, 0), // 10:00 AM
    new Date(2023, 0, 2, 11, 0), // 11:00 AM
  ];

  const timeSlots = generateTimeSlots(startTime, endTime, timeInterval);

  const handleTimeSlotClick = (timeSlot) => {
    const formattedTime = formatTime(timeSlot);
    const isConfirmed = window.confirm(
      `Do you want to confirm the selected time slot: ${formattedTime}?`
    );

    if (isConfirmed) {
      // User confirmed the selection, you can proceed with your logic here
      // For example, you can make an API request to book the appointment.
      console.log(`Appointment booked for: ${formattedTime}`);
    } else {
      // User canceled the selection, you can handle this case as needed
      console.log("Appointment selection canceled");
    }
  };

  const formatTime = (time) => {
    return time.toLocaleTimeString([], { hour: "2-digit", minute: "2-digit" });
  };

  return (
    <div>
      <h1>APPOINTMENT HOME</h1>
      <br></br>

      <h2>Please select a date for your appointment:</h2>
      <DatePicker></DatePicker>
      <br></br>

      <h2>Current available timeslots:</h2>
      <ul>{listItems}</ul>
      <br></br>

      <h2>Please select desired timeslot:</h2>
      <ul>
        {timeSlotsArray.map((timeSlot, index) => (
          <li key={index}>
            <button onClick={() => handleTimeSlotClick(timeSlot)}>
              {formatTime(timeSlot)}
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default AppointmentHome;
