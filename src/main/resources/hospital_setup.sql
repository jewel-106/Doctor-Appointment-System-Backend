-- Quick Setup Script for Multi-Hospital System
-- Run this in your MySQL database

-- Step 1: Add hospital_id column to users table
ALTER TABLE users ADD COLUMN hospital_id BIGINT NULL;
ALTER TABLE users ADD FOREIGN KEY (hospital_id) REFERENCES hospitals(id) ON DELETE SET NULL;

-- Step 2: Add hospital_id column to appointments table (for tracking which hospital the appointment is at)
ALTER TABLE appointments ADD COLUMN hospital_id BIGINT NULL;
ALTER TABLE appointments ADD FOREIGN KEY (hospital_id) REFERENCES hospitals(id) ON DELETE SET NULL;

-- Step 3: Insert Bangladesh Divisions
INSERT INTO divisions (name_en, name_bn, code) VALUES
('Dhaka', 'ঢাকা', 'DHA'),
('Chattogram', 'চট্টগ্রাম', 'CTG'),
('Rajshahi', 'রাজশাহী', 'RJS'),
('Khulna', 'খুলনা', 'KHL'),
('Barishal', 'বরিশাল', 'BAR'),
('Sylhet', 'সিলেট', 'SYL'),
('Rangpur', 'রংপুর', 'RNG'),
('Mymensingh', 'ময়মনসিংহ', 'MYM');

-- Step 4: Insert some major districts
INSERT INTO districts (division_id, name_en, name_bn, code) VALUES
-- Dhaka Division
(1, 'Dhaka', 'ঢাকা', 'DHA'),
(1, 'Gazipur', 'গাজীপুর', 'GAZ'),
(1, 'Narayanganj', 'নারায়ণগঞ্জ', 'NAR'),
-- Chattogram Division
(2, 'Chattogram', 'চট্টগ্রাম', 'CTG'),
(2, 'Cox\'s Bazar', 'কক্সবাজার', 'COX'),
-- Rajshahi Division
(3, 'Rajshahi', 'রাজশাহী', 'RJS'),
(3, 'Bogra', 'বগুড়া', 'BOG'),
-- Khulna Division
(4, 'Khulna', 'খুলনা', 'KHL'),
(4, 'Jessore', 'যশোর', 'JES');

-- Step 5: Insert sample upazilas (just a few for testing)
INSERT INTO upazilas (district_id, name_en, name_bn, code) VALUES
(1, 'Mohammadpur', 'মোহাম্মদপুর', 'MHP'),
(1, 'Dhanmondi', 'ধানমন্ডি', 'DHN'),
(1, 'Gulshan', 'গুলশান', 'GUL'),
(1, 'Mirpur', 'মিরপুর', 'MIR');

-- Step 6: Create Sample Hospitals
INSERT INTO hospitals (
    name, code, division_id, district_id, upazila_id,
    phone_primary, phone_secondary, email, emergency_hotline,
    address_line1, address_line2, postal_code,
    logo_url, tagline, website,
    facebook_url, twitter_url,
    operating_hours, is_active, is_featured, description
) VALUES
-- Hospital 1: Dhaka Medical Center
(
    'Dhaka Medical Center',
    'DMC-001',
    1, 1, 1, -- Dhaka > Dhaka > Mohammadpur
    '+880-2-9876543',
    '+880-1712-345678',
    'info@dhakamedical.com',
    '999',
    '123 Hospital Road',
    'Mohammadpur, Dhaka',
    '1207',
    '/assets/logos/dmc.png',
    'Your Health, Our Priority',
    'https://dhakamedical.com',
    'https://facebook.com/dhakamedical',
    'https://twitter.com/dhakamedical',
    '{"weekdays": "8:00 AM - 10:00 PM", "weekends": "9:00 AM - 6:00 PM", "emergency": "24/7"}',
    TRUE,
    TRUE,
    'Leading healthcare provider in Dhaka with state-of-the-art facilities and experienced doctors.'
),
-- Hospital 2: Chittagong General Hospital
(
    'Chittagong General Hospital',
    'CGH-002',
    2, 4, NULL, -- Chattogram > Chattogram > NULL
    '+880-31-654321',
    '+880-1813-456789',
    'info@chittagonggeneral.com',
    '999',
    '456 Medical Lane',
    'Agrabad, Chittagong',
    '4100',
    '/assets/logos/cgh.png',
    'Excellence in Healthcare',
    'https://chittagonggeneral.com',
    'https://facebook.com/chittagonggeneral',
    NULL,
    '{"weekdays": "9:00 AM - 9:00 PM", "weekends": "10:00 AM - 5:00 PM", "emergency": "24/7"}',
    TRUE,
    FALSE,
    'Trusted healthcare institution serving the people of Chittagong for over 20 years.'
);

-- Step 7: Create SUPER_ADMIN user
-- Password: admin123 (BCrypt hash - generate using BCryptPasswordEncoder)
-- You'll need to replace this with actual hash
INSERT INTO users (name, email, password, phone, role, hospital_id, created_at)
VALUES (
    'Super Admin',
    'superadmin@system.com',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', -- admin123
    '+880-1711-000000',
    'SUPER_ADMIN',
    NULL, -- Not tied to any hospital
    NOW()
);

-- Step 8: Create Hospital Admin for DMC
INSERT INTO users (name, email, password, phone, role, hospital_id, created_at)
VALUES (
    'DMC Admin',
    'admin@dhakamedical.com',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', -- admin123
    '+880-1712-111111',
    'ADMIN',
    1, -- Dhaka Medical Center
    NOW()
);

-- Step 9: Create Hospital Admin for CGH
INSERT INTO users (name, email, password, phone, role, hospital_id, created_at)
VALUES (
    'CGH Admin',
    'admin@chittagonggeneral.com',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', -- admin123
    '+880-1813-222222',
    'ADMIN',
    2, -- Chittagong General Hospital
    NOW()
);

-- Step 10: Create sample doctors
INSERT INTO users (name, email, password, phone, role, hospital_id, created_at)
VALUES 
(
    'Dr. Rahman',
    'dr.rahman@dhakamedical.com',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
    '+880-1713-333333',
    'DOCTOR',
    1, -- Dhaka Medical Center
    NOW()
),
(
    'Dr. Ahmed',
    'dr.ahmed@chittagonggeneral.com',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
    '+880-1814-444444',
    'DOCTOR',
    2, -- Chittagong General Hospital
    NOW()
);

-- Step 11: Create a sample patient (not tied to any hospital)
INSERT INTO users (name, email, password, phone, role, hospital_id, created_at)
VALUES (
    'John Patient',
    'patient@example.com',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', -- admin123
    '+880-1715-555555',
    'PATIENT',
    NULL, -- Not tied to any hospital
    NOW()
);

-- Step 12: Update existing users to assign hospitals (optional)
-- Uncomment and modify as needed
-- UPDATE users SET hospital_id = 1 WHERE role = 'ADMIN' AND hospital_id IS NULL;
-- UPDATE users SET hospital_id = 1 WHERE role = 'DOCTOR' AND hospital_id IS NULL;
-- UPDATE users SET hospital_id = NULL WHERE role = 'PATIENT';

-- Step 13: Verify setup
SELECT 'Divisions' as Type, COUNT(*) as Count FROM divisions
UNION ALL
SELECT 'Districts', COUNT(*) FROM districts
UNION ALL
SELECT 'Upazilas', COUNT(*) FROM upazilas
UNION ALL
SELECT 'Hospitals', COUNT(*) FROM hospitals
UNION ALL
SELECT 'SUPER_ADMIN Users', COUNT(*) FROM users WHERE role = 'SUPER_ADMIN'
UNION ALL
SELECT 'ADMIN Users', COUNT(*) FROM users WHERE role = 'ADMIN'
UNION ALL
SELECT 'DOCTOR Users', COUNT(*) FROM users WHERE role = 'DOCTOR'
UNION ALL
SELECT 'PATIENT Users', COUNT(*) FROM users WHERE role = 'PATIENT';

-- Display all test users
SELECT 
    name, 
    email, 
    role, 
    h.name as hospital_name,
    'admin123' as password_hint
FROM users u
LEFT JOIN hospitals h ON u.hospital_id = h.id
WHERE u.email IN (
    'superadmin@system.com',
    'admin@dhakamedical.com',
    'admin@chittagonggeneral.com',
    'dr.rahman@dhakamedical.com',
    'dr.ahmed@chittagonggeneral.com',
    'patient@example.com'
)
ORDER BY 
    FIELD(role, 'SUPER_ADMIN', 'ADMIN', 'DOCTOR', 'PATIENT'),
    hospital_name;
