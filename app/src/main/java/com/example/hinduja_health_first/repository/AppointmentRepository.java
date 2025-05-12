package com.example.hinduja_health_first.repository;

import com.example.hinduja_health_first.api.ApiClient;
import com.example.hinduja_health_first.api.ApiService;
import com.example.hinduja_health_first.api.models.Appointment;
import com.example.hinduja_health_first.api.models.Doctor;
import com.example.hinduja_health_first.api.models.Patient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentRepository {
    private final ApiService apiService;

    public AppointmentRepository() {
        apiService = ApiClient.getClient();
    }

    // Doctor operations
    public void getAllDoctors(DoctorsCallback callback) {
        apiService.getAllDoctors().enqueue(new Callback<List<Doctor>>() {
            @Override
            public void onResponse(Call<List<Doctor>> call, Response<List<Doctor>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Failed to fetch doctors");
                }
            }

            @Override
            public void onFailure(Call<List<Doctor>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void getDoctorsBySpecialty(String specialty, DoctorsCallback callback) {
        apiService.getDoctorsBySpecialty(specialty).enqueue(new Callback<List<Doctor>>() {
            @Override
            public void onResponse(Call<List<Doctor>> call, Response<List<Doctor>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Failed to fetch doctors by specialty");
                }
            }

            @Override
            public void onFailure(Call<List<Doctor>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    // Patient operations
    public void createPatient(Patient patient, PatientCallback callback) {
        apiService.createPatient(patient).enqueue(new Callback<Patient>() {
            @Override
            public void onResponse(Call<Patient> call, Response<Patient> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Failed to create patient");
                }
            }

            @Override
            public void onFailure(Call<Patient> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    // Appointment operations
    public void createAppointment(Appointment appointment, AppointmentCallback callback) {
        apiService.createAppointment(appointment).enqueue(new Callback<Appointment>() {
            @Override
            public void onResponse(Call<Appointment> call, Response<Appointment> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Failed to create appointment");
                }
            }

            @Override
            public void onFailure(Call<Appointment> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void checkTimeSlotAvailability(String doctorId, String date, String time,
                                          AvailabilityCallback callback) {
        apiService.checkTimeSlotAvailability(doctorId, date, time)
                .enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            callback.onAvailabilityChecked(response.body());
                        } else {
                            callback.onError("Failed to check availability");
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        callback.onError(t.getMessage());
                    }
                });
    }

    public void getAvailableTimeSlots(String doctorId, String date, TimeSlotsCallback callback) {
        apiService.getAvailableTimeSlots(doctorId, date).enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Failed to fetch available time slots");
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    // Callback interfaces
    public interface DoctorsCallback {
        void onSuccess(List<Doctor> doctors);

        void onError(String error);
    }

    public interface PatientCallback {
        void onSuccess(Patient patient);

        void onError(String error);
    }

    public interface AppointmentCallback {
        void onSuccess(Appointment appointment);

        void onError(String error);
    }

    public interface AvailabilityCallback {
        void onAvailabilityChecked(boolean isAvailable);

        void onError(String error);
    }

    public interface TimeSlotsCallback {
        void onSuccess(List<String> timeSlots);

        void onError(String error);
    }
} 