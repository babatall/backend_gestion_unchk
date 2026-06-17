-- =====================================================
-- INITIALISATION DES DONNÉES DE BASE
-- =====================================================

-- Insérer les RÔLES
INSERT INTO roles (name, description) VALUES
('ADMIN', 'Administrateur système'),
('RECTEUR', 'Recteur de l\'université'),
('DOYEN', 'Doyen de faculté'),
('CHEF_DEPARTMENT', 'Chef de département'),
('ENSEIGNANT', 'Enseignant'),
('ETUDIANT', 'Étudiant'),
('PERSONNEL_ADMINISTRATIF', 'Personnel administratif');

-- Insérer les PERMISSIONS
INSERT INTO permissions (name, description) VALUES
-- Permissions User
('READ_USERS', 'Lire les utilisateurs'),
('CREATE_USER', 'Créer un utilisateur'),
('UPDATE_USER', 'Modifier un utilisateur'),
('DELETE_USER', 'Supprimer un utilisateur'),

-- Permissions Course
('READ_COURSES', 'Lire les cours'),
('CREATE_COURSE', 'Créer un cours'),
('UPDATE_COURSE', 'Modifier un cours'),
('DELETE_COURSE', 'Supprimer un cours'),

-- Permissions Student
('READ_STUDENTS', 'Lire les étudiants'),
('CREATE_STUDENT', 'Créer un étudiant'),
('UPDATE_STUDENT', 'Modifier un étudiant'),
('DELETE_STUDENT', 'Supprimer un étudiant'),

-- Permissions Teacher
('READ_TEACHERS', 'Lire les enseignants'),
('CREATE_TEACHER', 'Créer un enseignant'),
('UPDATE_TEACHER', 'Modifier un enseignant'),
('DELETE_TEACHER', 'Supprimer un enseignant'),

-- Permissions Enrollment
('READ_ENROLLMENTS', 'Lire les inscriptions'),
('CREATE_ENROLLMENT', 'Créer une inscription'),
('UPDATE_ENROLLMENT', 'Modifier une inscription'),
('DELETE_ENROLLMENT', 'Supprimer une inscription'),

-- Permissions Grade
('READ_GRADES', 'Lire les notes'),
('CREATE_GRADE', 'Créer une note'),
('UPDATE_GRADE', 'Modifier une note'),
('DELETE_GRADE', 'Supprimer une note'),

-- Permissions System
('MANAGE_ROLES', 'Gérer les rôles'),
('MANAGE_PERMISSIONS', 'Gérer les permissions'),
('GENERATE_REPORTS', 'Générer les rapports'),
('SYSTEM_ADMIN', 'Administration système');

-- Assigner les PERMISSIONS aux RÔLES

-- ADMIN - Toutes les permissions
INSERT INTO role_permission (role_id, permission_id) 
SELECT r.id, p.id FROM roles r, permissions p 
WHERE r.name = 'ADMIN';

-- ETUDIANT - Permissions de lecture
INSERT INTO role_permission (role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p 
WHERE r.name = 'ETUDIANT' AND p.name IN ('READ_COURSES', 'READ_GRADES', 'READ_ENROLLMENTS');

-- ENSEIGNANT - Permissions lecture et création de notes
INSERT INTO role_permission (role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p 
WHERE r.name = 'ENSEIGNANT' AND p.name IN (
  'READ_COURSES', 'CREATE_COURSE', 'UPDATE_COURSE',
  'READ_STUDENTS', 'READ_GRADES', 'CREATE_GRADE', 'UPDATE_GRADE',
  'READ_ENROLLMENTS'
);

-- PERSONNEL_ADMINISTRATIF - Permissions de gestion de base
INSERT INTO role_permission (role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p 
WHERE r.name = 'PERSONNEL_ADMINISTRATIF' AND p.name IN (
  'READ_USERS', 'READ_COURSES', 'READ_STUDENTS', 'READ_TEACHERS',
  'READ_ENROLLMENTS', 'READ_GRADES'
);

-- RECTEUR - Permissions complètes
INSERT INTO role_permission (role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p 
WHERE r.name = 'RECTEUR';

-- DOYEN - Permissions complètes
INSERT INTO role_permission (role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p 
WHERE r.name = 'DOYEN';

-- CHEF_DEPARTMENT - Permissions complètes
INSERT INTO role_permission (role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p 
WHERE r.name = 'CHEF_DEPARTMENT';
