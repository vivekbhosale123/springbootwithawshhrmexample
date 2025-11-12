package com.vdb.controller;

import com.vdb.exception.RecordNotFoundException;
import com.vdb.model.Employee;
import com.vdb.service.EmployeeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeServiceImpl employeeService;

    @PostMapping("/signup")
    public ResponseEntity<Employee> signUp(@RequestBody Employee employee)
    {
        return ResponseEntity.ok(employeeService.signUp(employee));
    }

    @GetMapping("/signin/{empEmailId}/{empPassword}")
    public ResponseEntity<Boolean> signIn(@PathVariable String empEmailId,@PathVariable String empPassword)
    {
        return ResponseEntity.ok(employeeService.signIn(empEmailId, empPassword));
    }

    @GetMapping("/findbyid/{empId}")
    public ResponseEntity<Optional<Employee>> findById(@PathVariable int empId)
    {
        return ResponseEntity.ok(employeeService.findById(empId));
    }

    @GetMapping("/findall")
    public ResponseEntity<List<Employee>> findAll()
    {
        return ResponseEntity.ok(employeeService.findAll());
    }

    @GetMapping("/sortbyname")
    public ResponseEntity<List<Employee>> sortByName()
    {
        return ResponseEntity.ok(employeeService.findAll().stream().sorted(Comparator.comparing(Employee::getEmpName)).toList());
    }

    @GetMapping("/sortbysalary")
    public ResponseEntity<List<Employee>> sortBySalary()
    {
        return ResponseEntity.ok(employeeService.findAll().stream().sorted(Comparator.comparing(Employee::getEmpSalary)).toList());
    }

    @GetMapping("/findbyname")
    public ResponseEntity<List<Employee>> searchByName(@RequestParam(defaultValue = "vivek",required = false) String empName)
    {
        return ResponseEntity.ok(employeeService.findAll().stream().filter(emp->emp.getEmpName().equals(empName)).toList());
    }

    @GetMapping("/findbyemailId")
    public ResponseEntity<List<Employee>> searchByEmail(@RequestParam String empEmaildId)
    {
        return ResponseEntity.ok(employeeService.findAll().stream().filter(emp->emp.getEmpEmailId().equals(empEmaildId)).toList());
    }

//    @GetMapping("/findbydob")
//    public ResponseEntity<List<Employee>> searchByDOB(@RequestParam String empDOB) throws ParseException {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
//        Date empdob = simpleDateFormat.parse(empDOB);
//
//        List<Employee> result = employeeService.findAll().stream()
//                .filter(emp -> emp.getEmpDOB() != null && emp.getEmpDOB().compareTo(empdob) == 0)
//                .toList();
//
//        return ResponseEntity.ok(result);
//    }

    @GetMapping("/findbydob")
    public ResponseEntity<List<Employee>> searchByDOB(@RequestParam String empDOB) {

        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy");
        return ResponseEntity.ok(employeeService.findAll().stream().filter(emp->simpleDateFormat.format(emp.getEmpDOB()).equals(empDOB)).toList());
    }


    @GetMapping("/findbyanyinput/{input}")
    public ResponseEntity<List<Employee>> findByAnyInput(@PathVariable String input)
    {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy");

        return ResponseEntity.ok(employeeService.findAll().stream().filter(emp->emp.getEmpName().equals(input)
        || emp.getEmpAddress().equals(input)
        || emp.getEmpEmailId().equals(input)
        ||emp.getEmpPassword().equals(input)
        || String.valueOf(emp.getEmpId()).equals(input)
        ||simpleDateFormat.format(emp.getEmpDOB()).equals(input)).toList());
    }


    @PutMapping("/update/{empId}")
    public ResponseEntity<Employee> update(@PathVariable int empId,@RequestBody Employee employee)
    {
        Employee employee1=employeeService.findById(empId).orElseThrow(()->new RecordNotFoundException("Employee id Does Not exists"));

        employee1.setEmpName(employee.getEmpName());
        employee1.setEmpAddress(employee.getEmpAddress());
        employee1.setEmpSalary(employee.getEmpSalary());
        employee1.setEmpContactNumber(employee.getEmpContactNumber());
        employee1.setEmpDOB(employee.getEmpDOB());
        employee1.setEmpEmailId(employee.getEmpEmailId());
        employee1.setEmpPassword(employee.getEmpPassword());

        return ResponseEntity.ok(employeeService.update(empId, employee1));
    }

    @DeleteMapping("/deletebyid/{empId}")
    public ResponseEntity<String> deleteById(@PathVariable int empId)
    {
        employeeService.deleteById(empId);

        return ResponseEntity.ok("Employee Deleted Successfully");
    }

    @DeleteMapping("/deleteall")
    public ResponseEntity<String> deleteAll()
    {
        employeeService.deleteAll();

        return ResponseEntity.ok("All Employee Deleted Successfully");
    }

}
