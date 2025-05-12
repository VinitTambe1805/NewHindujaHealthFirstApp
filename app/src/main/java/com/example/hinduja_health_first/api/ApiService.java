package com.example.hinduja_health_first.api;

import com.example.hinduja_health_first.api.models.Appointment;
import com.example.hinduja_health_first.api.models.Doctor;
import com.example.hinduja_health_first.api.models.Patient;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    // Doctor endpoints
    @GET("doctors")
    Call<List<Doctor>> getAllDoctors();

    @GET("doctors/{doctorId}")
    Call<Doctor> getDoctorById(@Path("doctorId") String doctorId);

    @GET("doctors/specialty/{specialty}")
    Call<List<Doctor>> getDoctorsBySpecialty(@Path("specialty") String specialty);

    // Patient endpoints
    @POST("patients")
    Call<Patient> createPatient(@Body Patient patient);

    @GET("patients/{patientId}")
    Call<Patient> getPatientById(@Path("patientId") String patientId);

    @GET("patients/doctor/{doctorId}")
    Call<List<Patient>> getPatientsByDoctor(@Path("doctorId") String doctorId);

    // Appointment endpoints
    @POST("appointments")
    Call<Appointment> createAppointment(@Body Appointment appointment);

    @GET("appointments/doctor/{doctorId}")
    Call<List<Appointment>> getDoctorAppointments(@Path("doctorId") String doctorId);

    @GET("appointments/patient/{patientId}")
    Call<List<Appointment>> getPatientAppointments(@Path("patientId") String patientId);

    @GET("appointments/check-availability")
    Call<Boolean> checkTimeSlotAvailability(
        @Query("doctorId") String doctorId,
        @Query("appointmentDate") String appointmentDate,
        @Query("appointmentTime") String appointmentTime
    );

    @GET("appointments/doctor/{doctorId}/date/{date}")
    Call<List<Appointment>> getDoctorAppointmentsByDate(
        @Path("doctorId") String doctorId,
        @Path("date") String date
    );

    @GET("appointments/available-slots")
    Call<List<String>> getAvailableTimeSlots(
        @Query("doctorId") String doctorId,
        @Query("date") String date
    );
} 