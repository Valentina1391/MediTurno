import { Injectable, signal } from '@angular/core';

export type Lang = 'es' | 'en' | 'fr';

const TEXTOS: Record<Lang, Record<string, string>> = {
  es: {
    login: 'Iniciar sesión', correo: 'Correo electrónico', clave: 'Contraseña',
    errorLogin: 'Correo o contraseña incorrectos', salir: 'Salir', saltar: 'Saltar al contenido',
    reservar: 'Reservar cita', reservarBtn: 'Reservar', medico: 'Médico', fecha: 'Fecha y hora',
    seleccione: 'Seleccione un médico', misCitas: 'Mis citas', estado: 'Estado',
    accion: 'Acción', cancelar: 'Cancelar', sinCitas: 'No tienes citas',
    exito: 'Cita reservada correctamente', conflicto: 'El médico ya tiene una cita en ese horario',
    fallo: 'No se pudo reservar la cita',
    agenda: 'Agenda de citas', paciente: 'Paciente', atender: 'Marcar atendida',
    buscar: 'Buscar', noHay: 'No hay citas',
    admin: 'Administración', especialidades: 'Especialidades', nuevaEsp: 'Nueva especialidad',
    agregar: 'Agregar', medicos: 'Médicos', nombreMedico: 'Nombre del médico',
    especialidad: 'Especialidad', agregarMedico: 'Agregar médico', eliminar: 'Eliminar', nombre: 'Nombre',
    seleccioneEsp: 'Seleccione',
    errEsp: 'No se pudo crear la especialidad', errMed: 'No se pudo crear el médico',
    errElim: 'No se puede eliminar: el médico tiene citas',
    lema: 'Gestión de citas médicas',
    PROGRAMADA: 'Programada', CANCELADA: 'Cancelada', ATENDIDA: 'Atendida',
    calendario: 'Calendario mensual', citasDia: 'Citas del día', sinCitasDia: 'No hay citas este día', hoy: 'Hoy',
    confCancelar: '¿Seguro que desea cancelar esta cita?', confEliminar: '¿Seguro que desea eliminar este médico?',
    heroTitulo: 'Tu cita médica, sin filas ni llamadas', 
    heroTexto: 'Reserva, cancela y reprograma tus citas en línea, de forma rápida y segura.', 
    ingresar: 'Ingresar', 
    registrate: 'Crear cuenta', 
    f1t: 'Reserva en línea', 
    f1d: 'Elige médico, fecha y hora en pocos clics.', 
    f2t: 'Sin cruces de horario', 
    f2d: 'El sistema evita citas duplicadas con el mismo médico.', 
    f3t: 'Seguro y accesible', 
    f3d: 'Acceso por roles, datos protegidos y diseño para todas las personas.', 
    pie: 'Proyecto académico - Universidad Surcolombiana',
    errorGoogle: 'No se pudo iniciar sesión con Google', 
    o: 'o'
  },
  en: {
    login: 'Log in', correo: 'Email', clave: 'Password',
    errorLogin: 'Incorrect email or password', salir: 'Log out', saltar: 'Skip to content',
    reservar: 'Book appointment', reservarBtn: 'Book', medico: 'Doctor', fecha: 'Date and time',
    seleccione: 'Select a doctor', misCitas: 'My appointments', estado: 'Status',
    accion: 'Action', cancelar: 'Cancel', sinCitas: 'You have no appointments',
    exito: 'Appointment booked successfully', conflicto: 'The doctor already has an appointment at that time',
    fallo: 'The appointment could not be booked',
    agenda: 'Appointment schedule', paciente: 'Patient', atender: 'Mark as attended',
    buscar: 'Search', noHay: 'No appointments',
    admin: 'Administration', especialidades: 'Specialties', nuevaEsp: 'New specialty',
    agregar: 'Add', medicos: 'Doctors', nombreMedico: "Doctor's name",
    especialidad: 'Specialty', agregarMedico: 'Add doctor', eliminar: 'Delete', nombre: 'Name',
    seleccioneEsp: 'Select',
    errEsp: 'The specialty could not be created', errMed: 'The doctor could not be created',
    errElim: 'Cannot delete: the doctor has appointments',
    PROGRAMADA: 'Scheduled', CANCELADA: 'Cancelled', ATENDIDA: 'Attended',
    lema: 'Medical appointment management',
    calendario: 'Monthly calendar', citasDia: 'Appointments of the day', sinCitasDia: 'No appointments this day', hoy: 'Today',
    confCancelar: 'Are you sure you want to cancel this appointment?', confEliminar: 'Are you sure you want to delete this doctor?',
    heroTitulo: 'Your medical appointment, no queues or calls', 
    heroTexto: 'Book, cancel and reschedule your appointments online, quickly and securely.', 
    ingresar: 'Log in', 
    registrate: 'Sign up', 
    f1t: 'Online booking', 
    f1d: 'Choose doctor, date and time in a few clicks.', 
    f2t: 'No schedule conflicts', 
    f2d: 'The system prevents duplicate appointments with the same doctor.', 
    f3t: 'Secure and accessible', 
    f3d: 'Role-based access, protected data and design for everyone.', 
    pie: 'Academic project - Universidad Surcolombiana',
    errorGoogle: 'Could not sign in with Google', 
    o: 'or'
  },
  fr: {
    login: 'Se connecter', correo: 'Courriel', clave: 'Mot de passe',
    errorLogin: 'Courriel ou mot de passe incorrect', salir: 'Se déconnecter', saltar: 'Aller au contenu',
    reservar: 'Prendre rendez-vous', reservarBtn: 'Réserver', medico: 'Médecin', fecha: 'Date et heure',
    seleccione: 'Choisissez un médecin', misCitas: 'Mes rendez-vous', estado: 'Statut',
    accion: 'Action', cancelar: 'Annuler', sinCitas: "Vous n'avez aucun rendez-vous",
    exito: 'Rendez-vous réservé avec succès', conflicto: 'Le médecin a déjà un rendez-vous à cette heure',
    fallo: "Impossible de réserver le rendez-vous",
    agenda: 'Agenda des rendez-vous', paciente: 'Patient', atender: 'Marquer comme traité',
    buscar: 'Rechercher', noHay: 'Aucun rendez-vous',
    admin: 'Administration', especialidades: 'Spécialités', nuevaEsp: 'Nouvelle spécialité',
    agregar: 'Ajouter', medicos: 'Médecins', nombreMedico: 'Nom du médecin',
    especialidad: 'Spécialité', agregarMedico: 'Ajouter un médecin', eliminar: 'Supprimer', nombre: 'Nom',
    seleccioneEsp: 'Choisissez',
    errEsp: 'Impossible de créer la spécialité', errMed: 'Impossible de créer le médecin',
    errElim: 'Suppression impossible : le médecin a des rendez-vous',
    PROGRAMADA: 'Programmé', CANCELADA: 'Annulé', ATENDIDA: 'Traité',
    lema: 'Gestion des rendez-vous médicaux',
    calendario: 'Calendrier mensuel', citasDia: 'Rendez-vous du jour', sinCitasDia: "Aucun rendez-vous ce jour", hoy: "Aujourd'hui",
    confCancelar: 'Voulez-vous vraiment annuler ce rendez-vous ?', confEliminar: 'Voulez-vous vraiment supprimer ce médecin ?',
    heroTitulo: 'Votre rendez-vous médical, sans file ni appel', 
    heroTexto: 'Réservez, annulez et reportez vos rendez-vous en ligne, rapidement et en toute sécurité.', 
    ingresar: 'Se connecter', 
    registrate: 'Créer un compte', 
    f1t: 'Réservation en ligne', 
    f1d: 'Choisissez médecin, date et heure en quelques clics.', 
    f2t: 'Aucun conflit d’horaire', 
    f2d: 'Le système évite les rendez-vous en double avec le même médecin.', 
    f3t: 'Sécurisé et accessible', 
    f3d: 'Accès par rôles, données protégées et conception pour tous.', 
    pie: 'Projet académique - Universidad Surcolombiana',
    errorGoogle: 'Connexion avec Google impossible', 
    o: 'ou'
  }
};

@Injectable({ providedIn: 'root' })
export class Idioma {
  idiomas: { codigo: Lang; nombre: string }[] = [
    { codigo: 'es', nombre: 'Español' },
    { codigo: 'en', nombre: 'English' },
    { codigo: 'fr', nombre: 'Français' }
  ];

  lang = signal<Lang>((localStorage.getItem('lang') as Lang) || 'es');

  constructor() {
    document.documentElement.lang = this.lang();
  }

  /** Devuelve el texto de la clave en el idioma actual. */
  t(clave: string): string {
    return TEXTOS[this.lang()][clave] ?? clave;
  }

  cambiar(codigo: string) {
    const nuevo = codigo as Lang;
    this.lang.set(nuevo);
    localStorage.setItem('lang', nuevo);
    document.documentElement.lang = nuevo;
  }
}