import { DatePicker } from "@gsebdev/react-simple-datepicker";
import { useState, useEffect, useCallback, useRef, useContext } from "react";
import axios from "axios";
import { Link } from "react-router-dom";

function AppointmentHome() {
  const [auth, setAuth] = useState({
    user: sessionStorage.getItem('user'),
    pwd: sessionStorage.getItem('pwd'),
    personId: sessionStorage.getItem('person_id')
  });
  const [selectedDate, setSelectedDate] = useState(new Date(new Date().setDate(new Date().getDate() + 3)));
  const [timeSlots, setTimeSlots] = useState([

  ]);
  const [errormessagedatepicker, seterrormessagedatepicker] = useState();


  // const [availableTimeSlots, setAvailableTimeSlots] = useState([]);
  // const listItems = availableTimeSlots.map((dateTimeSlot) => (
  //   <li key={dateTimeSlot.id}>
  //     <p>
  //       <b>{dateTimeSlot.startDateTime}</b>
  //     </p>
  //   </li>
  // ));

  // Function to fetch time slots from the API
  const fetchTimeSlots = (date) => {
    seterrormessagedatepicker(null);
    console.log("intial:", date);
    // console.log("date instanceof Date" + date instanceof Date);
    const querydate = new Date(date).toLocaleDateString("en-CA");
    // console.log("after:", querydate);
    axios({
      method: "get",
      url: `http://localhost:8080/api/v1/appointment/queryAvailableTimeSlot/${querydate}`,
      auth: {
        // username: auth.user,
        // password: auth.password,
        username:"admin",
        password:"goodpass"
      },
    })
      //GET http://localhost:8080/api/v1/appointment/queryAvailableTimeSlot/2023-10-14
      .then((response) => {
        if (response.status === 200) {
          console.log("returned data: ", response.data);
          setTimeSlots(response.data);
        } else if (response.status === 404) {
        }

        // setTimeSlots(data.timeSlots); // Assuming your API response contains an array of time slots
      })
      .catch((error) => {
        console.error("Failed to fetch time slots from the API");
        setTimeSlots([]);

        console.error("An error occurred while fetching time slots:", error);
        alert(error.response.data.message);
        seterrormessagedatepicker(errormessagedatepicker);
      });
  };

  // Use useEffect to fetch time slots when the selected date changes
  // useEffect(() => {
  //   // window.confirm("fetching");
  //   fetchTimeSlots(selectedDate);
  // }, [selectedDate]);

  const handleTimeSlotClick = (timeSlot) => {
    const formattedTime = formatTime(new Date(timeSlot.startDateTime));
    const isConfirmed = window.confirm(
      `Do you want to confirm the selected time slot: ${formattedTime}?`
    );

    if (isConfirmed) {
      console.log(timeSlot.id);
      // POST http://localhost:8080/api/v1/appointment/bookNewAppointment/{auth.personId}/{timeslot.id}
      const bookingUrl = `http://localhost:8080/api/v1/appointment/bookNewAppointment/${auth.personId}/${timeSlot.id}`;

    axios
      .post(bookingUrl, {}, { auth: { username: auth.user, password: auth.pwd } })
      .then((response) => {
        if (response.status === 200) {
          console.log(`Appointment booked for: ${formattedTime}`);
          // You can display a success message to the user
          alert(`Appointment booked for: ${formattedTime}`);
        } else {
          // Handle other response statuses as needed
          console.error('Failed to book appointment.');
        }
      })
      .catch((error) => {
        console.error('An error occurred while booking the appointment:', error);
        // You can display an error message to the user
        alert('An error occurred while booking the appointment.');
      });
      // console.log(`Appointment booked for: ${formattedTime}`);
    } else {
      // User canceled the selection, you can handle this case as needed
      console.log("Appointment selection canceled");
    }
  };

  const formatTime = (time) => {
    return time.toLocaleTimeString([], { hour: "2-digit", minute: "2-digit" });
  };

  const handleDateSelection = (e) => {
    console.log(new Date(e.target.value));
    setSelectedDate(new Date(e.target.value));
    fetchTimeSlots(new Date(e.target.value));
  };

  return (
    <div>
      <h1>APPOINTMENT HOME</h1>
      <br></br>

      <h2>Please select a date for your appointment:</h2>
      <DatePicker
        id="datepicker-id"
        name="date-demo"
        onChange={handleDateSelection}
        value={selectedDate}
      ></DatePicker>
      <br></br>
      {errormessagedatepicker && <h1>{errormessagedatepicker}</h1>}

      {/* <h2>Current available timeslots:</h2>
      <ul>
        {timeSlots &&
          timeSlots.map((timeSlot) => (
            <li key={timeSlot.id}>
              <p>
                <b>{timeSlot.startDateTime.substring(11, 16)}</b>
              </p>
            </li>
          ))}
      </ul> */}

      <h2>Please select desired timeslot:</h2>
      <ul>
        {timeSlots &&
          timeSlots.map((timeSlot) => (
            <li key={timeSlot.id}>
              <button onClick={() => handleTimeSlotClick(timeSlot)}>
                {timeSlot.startDateTime.substring(11, 16)}
              </button>
            </li>
          ))}
      </ul>
      <Link to="/walkinHome">
        <button className="queue-button">Back</button>
      </Link>
    </div>
  );
}

export default AppointmentHome;
